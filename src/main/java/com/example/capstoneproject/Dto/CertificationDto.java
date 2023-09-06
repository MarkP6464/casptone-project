package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificationDto {
    private int id;

    private String Title;

    private String CertificateSource;

    private Date EndDate;

    private String CertificateRelevance;

    private CvStatus Status;

    private CvAddDto cv;
}
