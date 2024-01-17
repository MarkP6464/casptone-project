package com.example.capstoneproject.Dto;
import com.example.capstoneproject.Dto.responses.UserCoverLetterViewDto;
import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@Getter
@Setter
public class CoverLetterDto {
    private int id;
    private String title;
    private String dear;
    private LocalDate date;
    private String company;
    private String description;
    private String jobTitle;
    private String jobDescription;
    private UserCoverLetterViewDto user;
    private Integer cvId;
    private String resumeName;
}
