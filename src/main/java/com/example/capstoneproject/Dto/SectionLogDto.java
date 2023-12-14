package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Evaluate;
import com.example.capstoneproject.entity.Section;
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
public class SectionLogDto {
    private String Bullet;
    private Integer count;

    @Enumerated(EnumType.STRING)
    private SectionLogStatus Status;

    private Section section;
    private Evaluate evaluate;
}
