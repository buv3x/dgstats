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
@Table(schema = "datas", name = "round")
@Getter
@Setter
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Column(name = "number")
    private Integer number;

    @Column(name = "label")
    private String label;

    @Column(name = "label_abbreviated")
    private String labelAbbreviated;

    @Column(name = "date")
    private LocalDate date;
}
