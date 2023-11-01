package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContentDto {
    private String title;
    private String description;
    private List<ContentDetailDto> detail;
}
