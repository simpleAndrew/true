package org.andrew.truecall.app.config.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

public class DumpingScheduleSetup {

    @Valid
    @NotNull
    @JsonProperty
    private Long executionDelay;

    @Valid
    @NotNull
    @JsonProperty
    private TimeUnit executionUnit;

    @Valid
    @NotNull
    @JsonProperty
    private Long criticalSize;

    @Valid
    @NotNull
    @JsonProperty
    private Integer shiftInDaysToMove;

    public TimeUnit getExecutionUnit() {
        return executionUnit;
    }

    public Long getExecutionDelay() {
        return executionDelay;
    }

    public Long getCriticalSize() {
        return criticalSize;
    }

    public Integer getShiftInDaysToMove() {
        return shiftInDaysToMove;
    }
}
