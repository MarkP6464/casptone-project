package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExpertReviewViewDto {
    private Integer id;

    private String name;

    private String avatar;

    private String title;

    private Double star;

    private String description;

    private String company;

    private String priceMinMax;

    private List<PriceOptionViewDto> price;

    private Integer experience;

    private Integer numberReview;

    private Integer cvId;

    private List<ExpertReviewRatingViewDto> comments;
}
