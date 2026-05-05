package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StandingResponse {
    private Long teamId;
    private String teamName;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsConceded;
    private int points;
}