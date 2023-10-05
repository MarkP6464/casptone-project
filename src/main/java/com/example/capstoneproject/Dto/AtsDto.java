package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.SectionLogStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AtsDto {
    private String ats;
    private SectionLogStatus status;
}
