package com.example.capstoneproject.Dto;

import com.example.capstoneproject.Dto.responses.HistorySummaryViewDto;
import com.example.capstoneproject.entity.Cv;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class HistorySummaryDto {
    private Integer id;

    private String version;

    private String summary;
}
