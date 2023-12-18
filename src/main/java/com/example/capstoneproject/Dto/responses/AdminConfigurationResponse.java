package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class AdminConfigurationResponse {

    private Double vipMonthRatio;

    private Double vipYearRatio;

    private String apiKey;
}
