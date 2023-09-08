package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkillDto {

    private String Description;
}
