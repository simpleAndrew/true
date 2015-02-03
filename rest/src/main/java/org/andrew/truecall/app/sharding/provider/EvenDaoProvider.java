package org.andrew.truecall.app.sharding.provider;

import com.google.common.base.Optional;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Works in sharded environment. Based on requestedUserId it gives corresponding shard.
 * It makes even distribution over all available shards.
 *
 * Can't be changed on the fly and requires data changes to move on another sharding model.
 */
public class EvenDaoProvider implements DaoProvider {

    private final List<UserStatisticsDao> shardedDaoList;

    public EvenDaoProvider(List<UserStatisticsDao> daosToHandle) {
        this.shardedDaoList = Collections.unmodifiableList(new ArrayList<UserStatisticsDao>(daosToHandle));
    }

    @Override
    public UserStatisticsDao getDaoByRequestParam(Optional<Long> userId, Optional<Long> readUserId, Optional<DateTime> readTime) {

        long reviewedUserId = readUserId.isPresent() ? readUserId.get() : 0L;

        int shardIndex = (int) (reviewedUserId % shardedDaoList.size());

        return shardedDaoList.get(shardIndex);
    }
}
