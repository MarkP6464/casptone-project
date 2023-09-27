package com.example.capstoneproject.Dto.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkillViewDto {
    private int id;

    private String Description;

    private Boolean isDisplay;

}
