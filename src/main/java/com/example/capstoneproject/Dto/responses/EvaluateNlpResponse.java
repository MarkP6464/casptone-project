package com.example.capstoneproject.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EvaluateNlpResponse {
    private String personalPronoun;
    private String filler;
    private String passiveVoice;
}
