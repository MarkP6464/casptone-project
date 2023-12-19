package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.PriceOptionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExpertConfigViewDto {
    private String avatar;

    private String name;

    private String jobTitle;

    private String company;

    private String about;

    private Integer experiences;

    private Integer cvId;

    private String cv;

    private List<PriceOptionDto> price;

    private String bankName;

    private String bankAccountNumber;

    private String bankAccountName;
}
