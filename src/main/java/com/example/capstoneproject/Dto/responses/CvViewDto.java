package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvBodyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CvViewDto {
    private Integer id;
    private String resumeName;
    private String fieldOrDomain;
    private String experience;
    private String summary;
    private CvBodyDto cvBody;
}
