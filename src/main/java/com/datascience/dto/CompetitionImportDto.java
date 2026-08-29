package com.datascience.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionImportDto {

    private Long competitionId;
    private Boolean imported;
}
