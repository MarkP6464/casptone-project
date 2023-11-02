package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoverLetterUpdateDto {
    private String title;
    private String dear;
    private String date;
    private String company;
    private String description;
}
