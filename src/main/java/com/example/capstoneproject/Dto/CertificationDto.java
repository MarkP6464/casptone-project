package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class CertificationDto {
    private Boolean isDisplay = true;
    private Integer id;

    private Integer theOrder;

    private String Name;
    private String CertificateSource;

    private Integer EndYear;

    private String CertificateRelevance;
    private BasicStatus Status;
}
