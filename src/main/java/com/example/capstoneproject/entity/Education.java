package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Degree;

    private String CollegeName;

    private String Location;

    private int EndYear;

    private String Minor;

    private double Gpa;

    private String Description;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
