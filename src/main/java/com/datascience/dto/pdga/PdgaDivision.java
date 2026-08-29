package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaDivision {

    @JsonProperty("DivisionID")
    private Long divisionId;

    @JsonProperty("Division")
    private String division;

    @JsonProperty("DivisionName")
    private String divisionName;

    @JsonProperty("Players")
    private Integer players;

    @JsonProperty("IsPro")
    private Boolean pro;

    @JsonProperty("ShortName")
    private String shortName;

    @JsonProperty("LatestRound")
    private Integer latestRound;
}
