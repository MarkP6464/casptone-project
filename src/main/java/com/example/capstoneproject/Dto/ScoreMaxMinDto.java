package com.example.capstoneproject.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScoreMaxMinDto {
    private Integer score;

    private Integer max;

    private String assign;
}
