package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminConfigDashResponse {
    String income;
    Integer userLogin;
    Integer totalUser;
    List<AdminChartResponse> chart;
}
