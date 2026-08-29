package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaCompetitionInfo {

    @JsonProperty("TournamentId")
    private Long tournamentId;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("SimpleName")
    private String simpleName;

    @JsonProperty("StartDate")
    private LocalDate startDate;

    @JsonProperty("EndDate")
    private LocalDate endDate;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("Tier")
    private String tier;

    @JsonProperty("TotalPlayers")
    private Integer totalPlayers;

    @JsonProperty("Divisions")
    private List<PdgaDivision> divisions;

    @JsonProperty("RoundsList")
    private Map<String, PdgaRoundInfo> roundsList;

    @JsonProperty("Layouts")
    private List<PdgaLayout> layouts;
}
