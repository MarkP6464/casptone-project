package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CvMatchingDto {
    private Integer candidateId;
    private Integer cvId;
    private String name;
    private String avatar;
    private String jobTitle;
    private String company;
    private String about;
    private double score;
}
