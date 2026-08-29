package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaRoundResults {

    private String pool;
    private String division;
    private List<PdgaLayout> layouts;
    private List<PdgaLayoutHole> holes;
    private List<PdgaScore> scores;

    @JsonProperty("live_round_id")
    private Long liveRoundId;

    @JsonProperty("id")
    private Long divisionId;
}
