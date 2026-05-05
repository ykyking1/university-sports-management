package com.example.demo.entity;

import com.example.demo.enums.TournamentFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
public class Tournament extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TournamentFormat format;

    @ManyToMany
    @JoinTable(name = "tournament_teams",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams = new ArrayList<>();

    private LocalDateTime startDate;
    private boolean scheduleGenerated = false;
}
