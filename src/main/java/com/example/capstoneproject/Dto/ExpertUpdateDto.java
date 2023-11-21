package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ExpertUpdateDto {

    private String avatar;

    private String name;

    private String jobTitle;

    private String company;

    private String about;

    private Integer experiences;

    private Double price;
}
