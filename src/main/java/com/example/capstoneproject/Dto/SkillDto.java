package com.example.capstoneproject.Dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkillDto {
    private int id;

    private String Description;

    private Boolean isDisplay;
}
