package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogDto {
    private Integer id;

    private LocalDate timestamp;

    private Cv cv;

    private JobPosting jobPosting;
}
