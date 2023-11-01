package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SkillDto {
    private Integer id;
    private Integer theOrder;
    private Boolean isDisplay = true;

    private String Description;
}
