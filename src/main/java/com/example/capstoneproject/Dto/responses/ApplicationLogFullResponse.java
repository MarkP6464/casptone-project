package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.ApplicationLogStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogFullResponse {
    String candidateName;
    LocalDate applyDate;
    String note;
    String email;
    ApplicationLogStatus status;
    JobPostingNameViewDto jobPosting;

    HashMap<String, Object> cvs = new HashMap<>();
    HashMap<String, Object> coverLetters = new HashMap<>();
}
