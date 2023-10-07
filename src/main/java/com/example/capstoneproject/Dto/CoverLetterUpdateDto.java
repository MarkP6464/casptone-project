package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoverLetterUpdateDto {
    private String title;
    private Date date;
    private String company;
    private String description;
}
