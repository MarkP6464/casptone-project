package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor

@Getter
@Setter
@Entity
public class SourceWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Name;

    private String CourseLocation;

    private int EndYear;

    private String Skill;

    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
