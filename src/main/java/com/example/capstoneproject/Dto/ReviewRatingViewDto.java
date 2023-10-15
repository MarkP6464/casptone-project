package com.example.capstoneproject.Dto;

import lombok.*;

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

    private Date dateComment;

    private String comment;

    private UsersDto user;
}
