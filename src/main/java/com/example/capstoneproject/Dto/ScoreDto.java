package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScoreDto {
    ScoreMaxMinDto scoreContent;
    List<ContentDto> content;
    ScoreMaxMinDto scorePractice;
    List<ContentDto> practice;
    ScoreMaxMinDto scoreOptimization;
    List<ContentDto> optimization;
    ScoreMaxMinDto scoreFormat;
    List<ContentDto> format;
    String result;
    Integer totalScore;

}
