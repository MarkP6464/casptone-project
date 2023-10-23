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
    List<ContentDto> content;
    List<ContentDto> practice;
    List<ContentDto> optimization;

}
