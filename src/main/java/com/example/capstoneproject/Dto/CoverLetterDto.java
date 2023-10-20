package com.example.capstoneproject.Dto;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoverLetterDto {
    private int id;
    private String title;
    private Date date;
    private String company;
    private String description;
    private UserCoverLetterDto user;
}
