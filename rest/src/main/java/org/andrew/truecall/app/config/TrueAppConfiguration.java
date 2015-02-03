package org.andrew.truecall.app.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.andrew.truecall.app.config.modules.LimitsSetup;
import org.andrew.truecall.app.config.modules.StatisticStorageSetup;
import org.andrew.truecall.app.config.modules.UserStorageSetup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TrueAppConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private LimitsSetup queryLimits;

    @Valid
    @NotNull
    @JsonProperty
    private UserStorageSetup userStorage;

    @Valid
    @NotNull
    @JsonProperty
    private StatisticStorageSetup statisticStorage;

    public LimitsSetup getQueryLimits() {
        return queryLimits;
    }

    public UserStorageSetup getUserStorage() {
        return userStorage;
    }

    public StatisticStorageSetup getStatisticStorage() {
        return statisticStorage;
    }

}
