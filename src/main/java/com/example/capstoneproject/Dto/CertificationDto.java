package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificationDto {
    private int id;

    private String Name;
    private Boolean isDisplay;


    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;
}
