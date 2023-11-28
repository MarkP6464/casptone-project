package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Evaluate;
import com.example.capstoneproject.entity.JobDescription;
import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class CvDto {
    private Integer id;
    private String resumeName;
    private String fieldOrDomain;
    private String experience;
    private String companyName;
    private String jobTitle;
    private String linkin;
    private String summary;
    private BasicStatus status;
    private CvBodyDto cvBody;
    private ScoreDto evaluate;
    private JobDescription jobDescription;
    private UsersDto usersDto;
}
