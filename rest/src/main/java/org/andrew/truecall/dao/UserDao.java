package org.andrew.truecall.dao;

import org.andrew.truecall.representation.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface UserDao  {

    @SqlUpdate("insert into true_user (name) values (:userName)")
    public int createUser(@Bind("userName") String userName);

    @SqlQuery("select * from true_user u where u.id=:id")
    @Mapper(User.Mapper.class)
    public User getUser(@Bind("id") Long userId);

}
