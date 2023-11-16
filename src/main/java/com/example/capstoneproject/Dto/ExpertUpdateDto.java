package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpertUpdateDto {
    private String title;

    private String company;

    private String description;

    private Double price;

    private boolean availability;

    private Integer receive;

    private boolean punish;

    private LocalDate punishDate;
}
