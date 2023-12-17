package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AdminConfigurationRatioResponse {
    private Long vipMonthRatio;

    private Long vipYearRatio;

    private Double moneyRatio;
}
