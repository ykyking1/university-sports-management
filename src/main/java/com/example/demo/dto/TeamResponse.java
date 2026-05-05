package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TeamResponse {
    private Long id;
    private String name;
    private List<Long> playerIds;
    private Long captainId;
    private int playerCount;
}