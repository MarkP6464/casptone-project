package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificationDto {
    private String Name;

    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;
}
