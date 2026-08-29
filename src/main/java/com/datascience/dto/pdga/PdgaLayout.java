package com.datascience.dto.pdga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PdgaLayout {

    @JsonProperty("LayoutID")
    private Long layoutId;

    @JsonProperty("CourseID")
    private Long courseId;

    @JsonProperty("CourseName")
    private String courseName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Holes")
    private Integer holes;

    @JsonProperty("Par")
    private Integer par;

    @JsonProperty("Length")
    private Integer length;

    @JsonProperty("Units")
    private String units;

    @JsonProperty("Accuracy")
    private String accuracy;

    @JsonProperty("Details")
    private List<PdgaLayoutHole> details;

    @JsonProperty("Detail")
    private List<PdgaLayoutHole> detail;

    public List<PdgaLayoutHole> getHoleDetails() {
        return details != null ? details : detail;
    }
}
