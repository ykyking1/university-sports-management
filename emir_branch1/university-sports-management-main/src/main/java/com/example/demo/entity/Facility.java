package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
public class Facility extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String type;
    private int capacity;
    private double hourlyRate;
    private String operatingHours;
}
