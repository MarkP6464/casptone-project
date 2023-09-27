package com.example.capstoneproject.Dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkillDto {
    private Integer id;
    private Boolean isDisplay;

    private String Description;
}
