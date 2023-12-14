package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionLogStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BulletPointComponentDto {
    private String title;
    private String description;
    private String result;
    private Integer count;

    @Enumerated(EnumType.STRING)
    private SectionLogStatus status;
}
