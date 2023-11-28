package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScoreDto {
    Integer scoreContent;
    List<ContentDto> content;
    Integer scorePractice;
    List<ContentDto> practice;
    Integer scoreOptimization;
    List<ContentDto> optimization;
    Integer scoreFormat;
    List<ContentDto> format;
    String result;

}
