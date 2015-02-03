package org.andrew.truecall.dao;

import org.andrew.truecall.representation.UserView;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

public interface UserStatisticsDao {

    @SqlUpdate("insert into true_user_statistics (viewer_id, requested_user_id, read_date) values (:userId, :readUserId, :readDate)")
    void addRecord(
            @Bind("userId") Long userId,
            @Bind("readUserId") Long readUserId,
            @Bind("readDate") DateTime readTime
    );

    @SqlQuery("select * from true_user_statistics where requested_user_id=:id and read_date >= :tillDate order by read_date desc limit :limit")
    @Mapper(UserView.Mapper.class)
    List<UserView> getReadRecords(
            @Bind("id") Long userId,
            @Bind("limit") int readLimit,
            @Bind("tillDate")DateTime tillDate
    );

}
