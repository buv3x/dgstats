package com.datascience.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "datas", name = "competition_division")
@Getter
@Setter
public class CompetitionDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Column(name = "pdga_division_id")
    private Long pdgaDivisionId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "players")
    private Integer players;

    @Column(name = "pro")
    private Boolean pro;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "latest_round")
    private Integer latestRound;
}
