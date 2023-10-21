package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpertDto {

    private Integer id;

    private String title;

    private String description;

    private double price;

    private List<ReviewRatingViewDto> ratings;
}
