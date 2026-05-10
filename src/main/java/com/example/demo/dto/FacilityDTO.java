package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FacilityDTO {
    private Long id;
    private String name;
    private String type;
    private int capacity;
    private double hourlyRate;
    private String operatingHours;
}