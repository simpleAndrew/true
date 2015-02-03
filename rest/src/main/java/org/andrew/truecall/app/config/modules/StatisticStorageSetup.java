package org.andrew.truecall.app.config.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class StatisticStorageSetup {

    @Valid
    @NotNull
    @JsonProperty
    private List<DataSourceFactory> statisticsSourceFactory;

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory dumpSourceFactory;

    @Valid
    @NotNull
    @JsonProperty
    private String statisticsTableCreateScript;

    @Valid
    @NotNull
    @JsonProperty
    private List<String> statisticTableIndexScripts;

    @Valid
    @NotNull
    @JsonProperty
    private DumpingScheduleSetup dumpingProperties;

    public List<DataSourceFactory> getStatisticsSourceFactory() {
        return statisticsSourceFactory;
    }

    public String getStatisticsTableCreateScript() {
        return statisticsTableCreateScript;
    }

    public List<String> getStatisticTableIndexScripts() {
        return statisticTableIndexScripts;
    }

    public DataSourceFactory getDumpSourceFactory() {
        return dumpSourceFactory;
    }

    public DumpingScheduleSetup getDumpingProperties() {
        return dumpingProperties;
    }

}
