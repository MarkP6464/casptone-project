package com.example.capstoneproject.Dto;

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

    private String Content;
    private String Summary;
    private BasicStatus Status;

    private CvBodyDto cvBody;

    private TemplateViewDto template;


}
