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
@Table(schema = "datas", name = "round_division")
@Getter
@Setter
public class RoundDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_division_id")
    private CompetitionDivision competitionDivision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id")
    private Layout layout;

    @Column(name = "pdga_live_round_id")
    private Long pdgaLiveRoundId;

    @Column(name = "division_code")
    private String divisionCode;

    @Column(name = "pool")
    private String pool;
}
