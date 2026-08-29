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

import java.time.LocalDate;

@Entity
@Table(schema = "datas", name = "competition")
@Getter
@Setter
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pdga_id")
    private Long pdgaId;

    @Column(name = "name")
    private String name;

    @Column(name = "simple_name")
    private String simpleName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "country")
    private String country;

    @Column(name = "location")
    private String location;

    @Column(name = "tier")
    private String tier;

    @Column(name = "total_players")
    private Integer totalPlayers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
