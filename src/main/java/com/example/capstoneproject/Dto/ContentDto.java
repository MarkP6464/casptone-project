package com.example.capstoneproject.Dto;

import com.example.capstoneproject.Dto.responses.AnalyzeScoreDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContentDto {
    private Boolean critical;
    private String title;
    private String description;
    private Double score;
    private Integer max;
    private AnalyzeScoreDto analyze;
}
