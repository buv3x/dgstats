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
@Table(schema = "datas", name = "round_result")
@Getter
@Setter
public class RoundResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_division_id", nullable = false)
    private RoundDivision roundDivision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "pdga_result_id")
    private Long pdgaResultId;

    @Column(name = "pdga_round_id")
    private Long pdgaRoundId;

    @Column(name = "pdga_score_id")
    private Long pdgaScoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id")
    private Layout layout;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "round_score")
    private Integer roundScore;

    @Column(name = "round_to_par")
    private Integer roundToPar;

    @Column(name = "grand_total")
    private Integer grandTotal;

    @Column(name = "total_to_par")
    private Integer totalToPar;

    @Column(name = "round_rating")
    private Integer roundRating;

    @Column(name = "previous_place")
    private Integer previousPlace;

    @Column(name = "running_place")
    private Integer runningPlace;

    @Column(name = "tied")
    private Boolean tied;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "played")
    private Integer played;
}
