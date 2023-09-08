package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateViewDto {
    private int id;

    private String Name;

    private int AmountView;

    private String Content;
}
