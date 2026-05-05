package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchResultRequest {

    @NotNull
    private Long matchId;

    @Min(value = 0, message = "Skor negatif olamaz")
    private int homeScore;

    @Min(value = 0, message = "Skor negatif olamaz")
    private int awayScore;
}