package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CvAddDto {
    private int id;

    private String Content;

    private String Summary;

    private CvStatus Status;
}
