package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaScore {

    @JsonProperty("ResultID")
    private Long resultId;

    @JsonProperty("RoundID")
    private Long roundId;

    @JsonProperty("ScoreID")
    private Long scoreId;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Nationality")
    private String nationality;

    @JsonProperty("PDGANum")
    private Long pdgaNum;

    @JsonProperty("Rating")
    private Integer rating;

    @JsonProperty("LayoutID")
    private Long layoutId;

    @JsonProperty("GrandTotal")
    private Integer grandTotal;

    @JsonProperty("RoundScore")
    private Integer roundScore;

    @JsonProperty("RoundtoPar")
    private Integer roundToPar;

    @JsonProperty("ToPar")
    private Integer toPar;

    @JsonProperty("RoundRating")
    private Integer roundRating;

    @JsonProperty("PreviousPlace")
    private Integer previousPlace;

    @JsonProperty("RunningPlace")
    private Integer runningPlace;

    @JsonProperty("Tied")
    private Boolean tied;

    @JsonProperty("Completed")
    private Integer completed;

    @JsonProperty("Played")
    private Integer played;

    @JsonProperty("ProfileURL")
    private String profileUrl;

    @JsonProperty("HoleScores")
    private List<String> holeScores;
}
