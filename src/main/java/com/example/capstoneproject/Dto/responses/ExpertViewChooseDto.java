package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExpertViewChooseDto {
    private Integer id;

    private String name;

    private String avatar;

    private String title;

    private String company;

    private Double price;

    private Integer experience;

    private Integer numberReview;
}
