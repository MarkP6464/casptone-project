package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Evaluate;
import com.example.capstoneproject.entity.Section;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionLogDto {
    private String Bullet;
    private String Status;
    private Section section;
    private Evaluate evaluate;
}
