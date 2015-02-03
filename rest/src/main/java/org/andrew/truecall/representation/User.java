package org.andrew.truecall.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private final Long userId;
    private final String name;

    public User(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    @JsonProperty
    public Long getUserId() {
        return userId;
    }

    @JsonProperty
    public String getName() {
        return name;
    }


    public static class Mapper implements ResultSetMapper<User> {

        @Override
        public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            long id = r.getLong("id");
            String name = r.getString("name");
            return new User(id, name);
        }
    }
}
