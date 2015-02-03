package org.andrew.truecall.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserView {

    private Long viewerId;
    private Long requestedUserId;
    private DateTime timeViewed;

    public UserView(Long viewerId, Long requestedUserId, DateTime timeViewed) {
        this.viewerId = viewerId;
        this.requestedUserId = requestedUserId;
        this.timeViewed = timeViewed;
    }

    @JsonProperty
    public Long getViewerId() {
        return viewerId;
    }

    @JsonProperty
    public Long getRequestedUserId() {
        return requestedUserId;
    }

    @JsonProperty
    public DateTime getTimeViewed() {
        return timeViewed;
    }

    public static class Mapper implements ResultSetMapper<UserView> {
        @Override
        public UserView map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            long viewerId = r.getLong("viewer_id");
            long requested_user_id = r.getLong("requested_user_id");
            DateTime readDate = new DateTime(r.getTimestamp("read_date"));

            return new UserView(viewerId, requested_user_id, readDate);
        }
    }
}
