package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvResumeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CandidateViewDto {
    private String name;

    private String avatar;

    private String jobTitle;

    private String company;

    private String about;

    private boolean publish;

    private CvResumeDto[] cv;
}
