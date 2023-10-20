package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRequestAddDto {
    private LocalDate receivedDate;
    private String note;
}
