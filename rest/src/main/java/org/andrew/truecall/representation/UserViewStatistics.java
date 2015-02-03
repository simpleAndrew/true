package org.andrew.truecall.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserViewStatistics {

    private Long requestedUserId;
    private List<UserView> userViews;

    public UserViewStatistics(Long requestedUserId, List<UserView> userViews) {
        this.requestedUserId = requestedUserId;
        this.userViews = userViews;
    }

    @JsonProperty
    public List<UserView> getUserViews() {
        return userViews;
    }

    @JsonProperty
    public Long getRequestedUserId() {
        return requestedUserId;
    }
}
