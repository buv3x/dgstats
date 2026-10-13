package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaThrowTimelineResponse {

    @JsonProperty("scoreThrows")
    private List<PdgaScoreThrows> scoreThrows;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PdgaScoreThrows {

        private PdgaLiveHoleScore liveHoleScore;

        private List<PdgaHoleThrow> holeThrows;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PdgaLiveHoleScore {

        private Integer holeOrdinal;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PdgaHoleThrow {

        private Integer scoreOrdinal;

        private Integer penaltyCount;

        private PdgaLiveScoreThrow liveScoreThrow;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PdgaLiveScoreThrow {

        private Long throwId;

        private Long zoneId;

        private Integer distanceToTarget;

        private String penaltyType;

        private Long dropZoneId;

        private Integer dropDistanceToTarget;
    }
}
