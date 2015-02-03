package org.andrew.truecall.app.config.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UserStorageSetup {

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory userSourceFactory;

    @Valid
    @NotNull
    @JsonProperty
    private String userTableCreateScript;

    @Valid
    @JsonProperty
    private List<String> sampleUsersLoadScript;

    public DataSourceFactory getUserSourceFactory() {
        return userSourceFactory;
    }

    public String getUserTableCreateScript() {
        return userTableCreateScript;
    }

    public List<String> getSampleUsersLoadScript() {
        return sampleUsersLoadScript;
    }
}
