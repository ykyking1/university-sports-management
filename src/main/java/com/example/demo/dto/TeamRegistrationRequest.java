package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TeamRegistrationRequest {

    @NotBlank(message = "Takım adı boş olamaz")
    private String name;

    @NotEmpty(message = "En az bir oyuncu olmalı")
    private List<Long> playerIds;

    private Long captainId;
}