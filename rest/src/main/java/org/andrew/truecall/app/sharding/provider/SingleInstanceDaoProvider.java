package org.andrew.truecall.app.sharding.provider;

import com.google.common.base.Optional;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.joda.time.DateTime;

public class SingleInstanceDaoProvider implements DaoProvider{

    private final UserStatisticsDao dao;

    public SingleInstanceDaoProvider(UserStatisticsDao dao) {
        this.dao = dao;
    }

    @Override
    public UserStatisticsDao getDaoByRequestParam(Optional<Long> userId, Optional<Long> readUserId, Optional<DateTime> readTime) {
        return dao;
    }
}
