package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContentDto {
    private Boolean critical;
    private String title;
    private String description;
    private List<ContentDetailDto> detail;
}
