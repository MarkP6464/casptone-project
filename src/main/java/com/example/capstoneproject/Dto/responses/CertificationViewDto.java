package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class CertificationViewDto {
    private int id;

    private Boolean isDisplay;

    private String Name;

    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;

    private BasicStatus Status;
}
