package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ExpertReviewRatingViewDto {
    private Integer id;
    private String Name;
    private String avatar;
    private String comment;
    private Double score;
    private LocalDate dateComment;
}
