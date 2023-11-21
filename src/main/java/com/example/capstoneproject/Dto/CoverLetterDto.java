package com.example.capstoneproject.Dto;
import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoverLetterDto {
    private int id;
    private String title;
    private String dear;
    private LocalDate date;
    private String company;
    private String description;
    private UserCoverLetterDto user;
}
