package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRequestAddDto {
    private LocalDateTime deadline;
    private Double price;
    private String note;
}
