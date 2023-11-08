package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Evaluate {
    @Id
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double score;
    private double maxScore;
    private int condition1;
    private int condition2;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluate")
    private List<SectionLog> sectionLogs = new ArrayList<>();
}
