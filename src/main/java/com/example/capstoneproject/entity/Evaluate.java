package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Evaluate {
    @Id
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean critical;

    private Integer score;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluate")
    private List<SectionLog> sectionLogs = new ArrayList<>();
}
