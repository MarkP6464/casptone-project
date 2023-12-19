package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AdminDateChartResponse {
    LocalDate start;
    LocalDate end;
}
