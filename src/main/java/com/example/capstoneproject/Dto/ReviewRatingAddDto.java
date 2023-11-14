package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRatingAddDto {

    private Double score;

    private String comment;
}
