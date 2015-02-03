package org.andrew.truecall.app.sharding;

import com.google.common.base.Optional;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.andrew.truecall.representation.UserView;
import org.andrew.truecall.app.sharding.provider.DaoProvider;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Proxy to hind nuances of routing from class client.
 * Uses provided DaoProvider to route requests.
 */
public class UserStatisticsProxyDao implements UserStatisticsDao {

    private final DaoProvider provider;

    public UserStatisticsProxyDao(DaoProvider provider) {
        this.provider = provider;
    }

    @Override
    public void addRecord(Long userId, Long readUserId, DateTime readTime) {
        UserStatisticsDao dao = provider.getDaoByRequestParam(Optional.of(userId), Optional.of(readUserId), Optional.of(readTime));
        dao.addRecord(userId, readUserId, readTime);
    }

    @Override
    public List<UserView> getReadRecords(Long userId, int readLimit, DateTime tillDate) {
        UserStatisticsDao dao = provider.getDaoByRequestParam(Optional.<Long>absent(), Optional.of(userId), Optional.<DateTime>absent());
        return dao.getReadRecords(userId, readLimit, tillDate);
    }
}
