package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvaluateViewDto {
    private Integer id;
    private Boolean criteria;
    private String description;
    private String title;
    private Integer score;
}
