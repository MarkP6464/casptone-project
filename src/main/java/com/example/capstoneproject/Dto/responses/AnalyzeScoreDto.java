package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.ContentDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeScoreDto {
    private Integer count;

    private Integer experience;

    private Integer project;

    private Integer involvement;

    private Integer total;

    private List<ContentDetailDto> moreInfos;
}
