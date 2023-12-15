package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogResponse {
    String candidateName;
    LocalDate applyDate;
    String note;
    String email;

    HashMap<String, Object> cvs = new HashMap<>();
    HashMap<String, Object> coverLetters = new HashMap<>();
}
