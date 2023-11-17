package com.example.capstoneproject.Dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CvBodyApplyDto {

    private String resumeName;

    private String cv;
}
