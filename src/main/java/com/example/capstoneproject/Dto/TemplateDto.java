package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateDto {
    private int id;

    private String Name;

    private int AmountView;

    private String Content;
    private CvStatus Status;
}
