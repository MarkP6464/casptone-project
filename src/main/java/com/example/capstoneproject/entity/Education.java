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

    private double Gpa;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
