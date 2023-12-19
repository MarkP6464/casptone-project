package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    private List<PriceOptionDto> price;

    private Integer cvId;

    private String bankName;

    private String bankAccountNumber;

    private String bankAccountName;
}
