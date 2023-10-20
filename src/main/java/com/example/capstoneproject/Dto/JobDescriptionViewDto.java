package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobDescriptionViewDto {
    private String title;
    private String description;
    List<AtsDto> ats;
}
