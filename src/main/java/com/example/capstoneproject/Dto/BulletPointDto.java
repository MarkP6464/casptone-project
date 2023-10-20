package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionLogStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BulletPointDto {
    private String title;
    private String description;
    private String result;

    @Enumerated(EnumType.STRING)
    private SectionLogStatus status;

}
