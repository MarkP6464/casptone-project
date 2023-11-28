package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogResponse {
    String candidateName;
    LocalDate applyDate;
    String note;
    String email;

    List<HashMap<String, String>> cvs;
    List<HashMap<String, String>> coverLetters;
}
