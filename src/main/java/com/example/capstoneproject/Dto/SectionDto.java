package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionEvaluate;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionDto {
    private int Id;
    private SectionEvaluate TypeName;
    private int TypeId;
    private String Title;
}
