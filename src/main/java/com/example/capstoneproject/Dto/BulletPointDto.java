package com.example.capstoneproject.Dto;

import lombok.*;

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
    private String status;

}
