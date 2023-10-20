package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRatingDto {
    private Integer id;

    private double score;

    private String comment;
}
