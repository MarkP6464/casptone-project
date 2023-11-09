package com.example.capstoneproject.Dto;
import lombok.*;


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
    private String date;
    private String company;
    private String description;
    private UserCoverLetterDto user;
}
