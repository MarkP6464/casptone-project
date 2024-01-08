package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class SkillViewDto {
    private int id;

    private String Description;

    private Boolean isDisplay;

    private BasicStatus Status;
}
