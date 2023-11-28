package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionEvaluate;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionAddScoreLogDto {
    @Enumerated(EnumType.STRING)
    private SectionEvaluate typeName;
    private Integer typeId;
}
