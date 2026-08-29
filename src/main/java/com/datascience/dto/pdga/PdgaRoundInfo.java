package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaRoundInfo {

    @JsonProperty("Number")
    private Integer number;

    @JsonProperty("Label")
    private String label;

    @JsonProperty("LabelAbbreviated")
    private String labelAbbreviated;

    @JsonProperty("Date")
    private LocalDate date;
}
