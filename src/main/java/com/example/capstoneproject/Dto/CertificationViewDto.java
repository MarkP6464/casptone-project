package com.example.capstoneproject.Dto;

import lombok.*;

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
}
