package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRatingViewDto {
    private Integer id;

    private double score;

    private LocalDate dateComment;

    private String comment;

    private UsersDto user;
}
