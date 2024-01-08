package com.example.capstoneproject.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionItemDto {

    private Long id;
    private Long theOrder;
    private Boolean isDisplay;
    private String duration;
    private String location;
    private String subTitle;
    private String title;
    private String description;
}
