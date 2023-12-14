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

    private String jobTitle;

    private Double star;

    private String company;

    private String price;

    private Integer experience;

    private Integer numberReview;
}
