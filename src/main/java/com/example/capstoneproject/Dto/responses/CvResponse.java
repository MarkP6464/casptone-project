package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CvResponse {
    Integer id;
    String resume;
    String jobTitle;
    String company;
    String jobDescription;
}
