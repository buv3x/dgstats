package com.datascience.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "datas", name = "competition_import")
@Getter
@Setter
public class CompetitionImport {

    @Id
    @Column(name = "competition_id", nullable = false)
    private Long competitionId;

    @Column(name = "imported", nullable = false)
    private Boolean imported = Boolean.FALSE;
}
