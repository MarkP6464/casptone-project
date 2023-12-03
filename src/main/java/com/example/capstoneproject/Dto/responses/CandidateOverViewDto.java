package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CandidateOverViewDto {
    private Integer id;
    private String name;

    private String avatar;

    private String jobTitle;

    private String company;

    private String about;
}
