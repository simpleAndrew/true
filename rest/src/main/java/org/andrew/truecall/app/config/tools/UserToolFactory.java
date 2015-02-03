package org.andrew.truecall.app.config.tools;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.andrew.truecall.app.config.TrueAppConfiguration;
import org.andrew.truecall.dao.UserDao;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/**
 * Factory class to setup User environment.
 * Builds initial DB table and load sample data.
 */
public class UserToolFactory {

    public static final String USERS_DB = "usersDB";

    private final TrueAppConfiguration configuration;
    private final Environment environment;
    private DBI userDb;

    public static UserToolFactory createFactory(TrueAppConfiguration configuration, Environment environment) throws ClassNotFoundException {
        UserToolFactory userToolFactory = new UserToolFactory(configuration, environment);
        userToolFactory.init();
        return userToolFactory;
    }

    private UserToolFactory(TrueAppConfiguration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    private void init() throws ClassNotFoundException {
        DBIFactory dbiFactory = new DBIFactory();
        userDb = dbiFactory.build(environment, configuration.getUserStorage().getUserSourceFactory(), USERS_DB);
        Handle userHandle = userDb.open();
        setupUsers(configuration, userHandle);
        userHandle.close();
    }

    public UserDao getUserDao() {
        return userDb.open(UserDao.class);
    }

    private void setupUsers(TrueAppConfiguration configuration, Handle handle) {
        handle.execute(configuration.getUserStorage().getUserTableCreateScript());

        for (String dataQueries : configuration.getUserStorage().getSampleUsersLoadScript()) {
            handle.insert(dataQueries);
        }
    }

}
