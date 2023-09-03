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
public class Involvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String OrganizationRole;

    private String OrganizationName;

    private Date StartDate;

    private Date EndDate;

    private String CollegeLocation;

    private String Description;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
