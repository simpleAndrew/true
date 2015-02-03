package org.andrew.truecall.app.config.tools;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.andrew.truecall.app.config.TrueAppConfiguration;
import org.andrew.truecall.app.scheduled.ScheduledCleaner;
import org.andrew.truecall.app.sharding.UserStatisticsProxyDao;
import org.andrew.truecall.app.sharding.provider.EvenDaoProvider;
import org.andrew.truecall.dao.UserStatisticsBatchDao;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Factory class that responsible for setup of DB environments to store statistics.
 * Creates DB structure with indexes.
 *
 * Allows then to create DAO classes to work with Statistics.
 */
public class StatisticToolFactory {

    private static final String STATISTICS_DB = "statisticsDB";
    private static final String STATISTICS_DUMP_DB = "statisticsDumpDB";

    private List<DBI> statDBIs;
    private DBI statDumpDBI;

    private final TrueAppConfiguration configuration;
    private final Environment environment;

    public static StatisticToolFactory createFactory(TrueAppConfiguration configuration, Environment environment) throws ClassNotFoundException {
        StatisticToolFactory statisticToolFactory = new StatisticToolFactory(configuration, environment);
        statisticToolFactory.init();
        return statisticToolFactory;
    }

    private StatisticToolFactory(TrueAppConfiguration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    private void init() throws ClassNotFoundException{
        DBIFactory dbiFactory = new DBIFactory();

        statDBIs = setupStatisticsEnvironment(configuration, environment, dbiFactory);

        statDumpDBI = dbiFactory.build(
                environment,
                configuration.getStatisticStorage().getDumpSourceFactory(),
                STATISTICS_DUMP_DB);

        setupStatisticsDB(configuration, statDumpDBI);

    }

    public UserStatisticsDao getShardedDao() {
        List<UserStatisticsDao> daos = Lists.transform(statDBIs, new Function<DBI, UserStatisticsDao>() {
            @Nullable
            @Override
            public UserStatisticsDao apply(@Nullable DBI input) {
                return input.open(UserStatisticsDao.class);
            }
        });

        return new UserStatisticsProxyDao(new EvenDaoProvider(new ArrayList<UserStatisticsDao>(daos)));
    }

    public UserStatisticsBatchDao getDumpDao() {
        return statDumpDBI.open(UserStatisticsBatchDao.class);
    }

    public Runnable getScheduledDumper() {
        return new ScheduledCleaner(getBatchDaos(),
                getDumpDao(),
                configuration.getStatisticStorage().getDumpingProperties().getCriticalSize(),
                configuration.getStatisticStorage().getDumpingProperties().getShiftInDaysToMove());
    }

    public List<UserStatisticsBatchDao> getBatchDaos() {
        List<UserStatisticsBatchDao> batchDaos = Lists.transform(statDBIs, new Function<DBI, UserStatisticsBatchDao>() {
            @Nullable
            @Override
            public UserStatisticsBatchDao apply(@Nullable DBI input) {
                return input.open(UserStatisticsBatchDao.class);
            }
        });
        return new ArrayList<UserStatisticsBatchDao>(batchDaos);
    }

    private List<DBI> setupStatisticsEnvironment(TrueAppConfiguration configuration, Environment environment, DBIFactory dbiFactory) throws ClassNotFoundException {
        List<DBI> statDBIs = new LinkedList<DBI>();
        int counter = 0;
        for (DataSourceFactory statSources : configuration.getStatisticStorage().getStatisticsSourceFactory()) {
            DBI build = dbiFactory.build(environment, statSources, STATISTICS_DB + counter);
            DBI statDB = setupStatisticsDB(configuration, build);
            statDBIs.add(statDB);
            counter++;
        }
        return statDBIs;
    }

    private DBI setupStatisticsDB(TrueAppConfiguration configuration, DBI statDBI) throws ClassNotFoundException {
        Handle statHandler = statDBI.open();
        setupStatisticsSchema(configuration, statHandler);
        statHandler.close();
        return statDBI;
    }

    private void setupStatisticsSchema(TrueAppConfiguration configuration, Handle handle) {
        handle.execute(configuration.getStatisticStorage().getStatisticsTableCreateScript());

        for (String indexScripts : configuration.getStatisticStorage().getStatisticTableIndexScripts()) {
            handle.execute(indexScripts);
        }
    }
}
