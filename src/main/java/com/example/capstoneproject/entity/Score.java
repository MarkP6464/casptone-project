package com.example.capstoneproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer content;

    private Integer optimization;

    private Integer practice;

    private Integer format;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String result;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_id")
    private Cv cv;

}
