package com.example.capstoneproject.Dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerViewDto {
    private int id;

    private String name;
}
