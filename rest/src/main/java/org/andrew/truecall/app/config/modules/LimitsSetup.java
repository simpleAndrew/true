package org.andrew.truecall.app.config.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LimitsSetup {

    @Valid
    @NotNull
    @JsonProperty
    private int requestLimitation;

    @Valid
    @NotNull
    @JsonProperty
    private int daysTracked;

    public int getRequestLimitation() {
        return requestLimitation;
    }

    public int getDaysTracked() {
        return daysTracked;
    }
}
