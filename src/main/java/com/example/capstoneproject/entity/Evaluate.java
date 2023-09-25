package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Evaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String more;
    private String description;
    private double score;
    private double maxScore;
    private int condition1;
    private int condition2;

    @ManyToOne
    @JoinColumn(name = "experience_id")
    private Experience experience;
}
