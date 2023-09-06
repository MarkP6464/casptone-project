package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificationViewDto {
    private int id;

    private String Title;

    private String CertificateSource;

    private Date EndDate;

    private String CertificateRelevance;

    private CvStatus Status;
}
