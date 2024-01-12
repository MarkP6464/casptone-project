package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDateViewDto {
    private Integer id;

    private Timestamp timestamp;
    private JobPostingNameViewDto jobPosting;
}
