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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Title;

    private String Organization;

    private Date StartDate;

    private Date EndDate;

    private String Url;

    private String Description;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;

}
