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
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Name;

    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
