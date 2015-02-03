package org.andrew.truecall.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.andrew.truecall.app.config.TrueAppConfiguration;
import org.andrew.truecall.dao.UserDao;
import org.andrew.truecall.dao.UserStatisticsBatchDao;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.andrew.truecall.web.filter.UserIdRequiredFilter;
import org.andrew.truecall.web.resource.UserResource;
import org.andrew.truecall.service.UserService;
import org.andrew.truecall.app.config.tools.StatisticToolFactory;
import org.andrew.truecall.app.config.tools.UserToolFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TruaApplication extends Application<TrueAppConfiguration> {

    private static final int DEFAULT_NUMBER_OF_DUMPING_THREADS = 1;
    private static final String APP_NAME = "a.shchyolok-sample-dropw-truecaller";

    public static void main(String[] args) throws Exception {
        new TruaApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TrueAppConfiguration> bootstrap) {
    }

    @Override
    public void run(TrueAppConfiguration configuration, Environment environment) throws Exception {

        StatisticToolFactory statisticToolFactory = StatisticToolFactory.createFactory(configuration, environment);
        UserToolFactory userToolFactory = UserToolFactory.createFactory(configuration, environment);

        setupDumpingJob(environment, statisticToolFactory.getScheduledDumper(), configuration);

        UserService userService = createUserService(configuration, userToolFactory, statisticToolFactory);
        UserStatisticsBatchDao dumpDao = statisticToolFactory.getDumpDao();

        registerWebComponents(environment, userService, dumpDao);

    }

    /**
     * Registers User Resource and Filter to filter out requests without userId
     * @param environment
     * @param userService
     * @param dumpDao
     */
    private void registerWebComponents(Environment environment, UserService userService, UserStatisticsBatchDao dumpDao) {

        environment.jersey().register(new UserResource(userService, dumpDao));
        environment.jersey().getResourceConfig().getContainerRequestFilters().add(new UserIdRequiredFilter());
    }

    private UserService createUserService(TrueAppConfiguration configuration, UserToolFactory userToolFactory, StatisticToolFactory statisticToolFactory) {

        UserDao userDao = userToolFactory.getUserDao();
        UserStatisticsDao proxyDao = statisticToolFactory.getShardedDao();

        int requestLimitation = configuration.getQueryLimits().getRequestLimitation();
        int daysTracked = configuration.getQueryLimits().getDaysTracked();

        return new UserService(proxyDao, userDao, requestLimitation, daysTracked);
    }

    private void setupDumpingJob(Environment environment, Runnable scheduledDumper, TrueAppConfiguration config) {
        ScheduledExecutorService ses = environment.lifecycle().
                scheduledExecutorService("statistic-dump-thread-%d").threads(DEFAULT_NUMBER_OF_DUMPING_THREADS).build();

        Long delay = config.getStatisticStorage().getDumpingProperties().getExecutionDelay();
        TimeUnit unit = config.getStatisticStorage().getDumpingProperties().getExecutionUnit();

        ses.scheduleWithFixedDelay(scheduledDumper, delay, delay, unit);
    }


    @Override
    public String getName() {
        return APP_NAME;
    }
}
