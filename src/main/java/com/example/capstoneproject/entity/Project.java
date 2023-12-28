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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    @NotNull
    private String Title;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Organization;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String ProjectUrl;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;


}
