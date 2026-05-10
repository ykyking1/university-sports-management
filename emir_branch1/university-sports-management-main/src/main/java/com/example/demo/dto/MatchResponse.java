package com.example.demo.dto;

import com.example.demo.enums.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MatchResponse {
    private Long id;
    private String homeTeamName;
    private String awayTeamName;
    private int homeScore;
    private int awayScore;
    private int roundNumber;
    private MatchStatus status;
    private LocalDateTime scheduledTime;
}