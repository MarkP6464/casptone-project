package com.example.capstoneproject.Dto;

import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogCustomDto {
    List<ApplicationLogResponse> list;
}
