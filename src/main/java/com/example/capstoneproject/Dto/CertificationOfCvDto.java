package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Cv;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificationOfCvDto {
    private int id;

    private Certification certification;

    private Cv cv;
}
