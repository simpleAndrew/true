package org.andrew.truecall.app.sharding.provider;

import com.google.common.base.Optional;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.joda.time.DateTime;

/**
 * Strategy class to get DAO based on incoming parameters.
 */
public interface DaoProvider {

    UserStatisticsDao getDaoByRequestParam(Optional<Long> userId, Optional<Long> readUserId, Optional<DateTime> readTime);
}
