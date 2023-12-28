package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@NoArgsConstructor

@Getter
@Setter
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Degree;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String CollegeName;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Location;

    private Integer EndYear;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Minor;


    @Column(columnDefinition = "DOUBLE", nullable = true)
    private Double Gpa;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
