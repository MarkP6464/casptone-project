package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionEvaluate;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContentDetailDto {
    @Enumerated(EnumType.STRING)
    private SectionEvaluate typeName;
    private int typeId;
    private String title;
}
