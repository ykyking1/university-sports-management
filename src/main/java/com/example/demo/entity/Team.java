package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> players = new ArrayList<>();

    @ManyToOne
    private User captain;
}
