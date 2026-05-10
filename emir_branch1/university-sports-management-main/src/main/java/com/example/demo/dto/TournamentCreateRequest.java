package com.example.demo.dto;

import com.example.demo.enums.TournamentFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class TournamentCreateRequest {

    @NotBlank(message = "Turnuva adı boş olamaz")
    private String name;

    @NotNull(message = "Format seçilmeli")
    private TournamentFormat format;

    private LocalDateTime startDate;
}