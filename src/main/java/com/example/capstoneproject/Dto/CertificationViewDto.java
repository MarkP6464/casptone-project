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

    private String Name;

    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;

    private CvStatus Status;
}
