package com.example.demo.entity;

import com.example.demo.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match extends BaseEntity {
    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private Team homeTeam;

    @ManyToOne
    private Team awayTeam;

    private int homeScore;
    private int awayScore;
    private int roundNumber;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED;

    private LocalDateTime scheduledTime;
}
