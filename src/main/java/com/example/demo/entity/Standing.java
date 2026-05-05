package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standings")
@Getter
@Setter
@NoArgsConstructor
public class Standing extends BaseEntity {
    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private Team team;

    private int played = 0;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;
    private int goalsScored = 0;
    private int goalsConceded = 0;
    private int points = 0;
}
