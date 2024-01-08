package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomizeSectionDto {
    private Integer id;
    private String sectionName;
    private List<SectionItemDto> sectionData;
}
