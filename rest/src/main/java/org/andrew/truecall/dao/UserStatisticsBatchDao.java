package org.andrew.truecall.dao;

import org.andrew.truecall.representation.UserView;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * Dao to perform batch modifications, mostly copying information from read tables to archived dump.
 * Separated from main DAO to isolate it's API
 */
public interface UserStatisticsBatchDao {

    @SqlQuery("select count(*) from true_user_statistics where read_date < :fromDate")
    Long measureTail(
            @Bind("fromDate")DateTime tillDate
    );

    @SqlQuery("select * from true_user_statistics where read_date < :fromDate")
    @Mapper(UserView.Mapper.class)
    Iterable<UserView> readTail(
            @Bind("fromDate")DateTime tillDate
    );

    @SqlUpdate("delete from true_user_statistics where read_date < :fromDate")
    void clearTail(
            @Bind("fromDate")DateTime tillDate
    );

    @SqlBatch("insert into true_user_statistics (viewer_id, requested_user_id, read_date) values (:viewerId, :requestedUserId, :timeViewed)")
    void insertAll(@BindBean Iterable<UserView> views);

}
