package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CandidateDto {
    private String name;

    private String avatar;

    private String jobTitle;

    private String company;

    private String about;

    private boolean publish;
}
