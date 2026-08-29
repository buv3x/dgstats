package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaLayoutHole {

    @JsonProperty("Hole")
    private String hole;

    @JsonProperty("HoleOrdinal")
    private Integer holeOrdinal;

    @JsonProperty("Label")
    private String label;

    @JsonProperty("Par")
    private Integer par;

    @JsonProperty("Length")
    private Integer length;

    @JsonProperty("Ordinal")
    private Integer ordinal;

    public Integer resolvedOrdinal() {
        if (holeOrdinal != null) {
            return holeOrdinal;
        }
        return ordinal;
    }
}
