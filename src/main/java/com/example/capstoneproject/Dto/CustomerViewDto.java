package com.example.capstoneproject.Dto;

import lombok.*;

import javax.persistence.Column;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerViewDto {
    private int id;

    private String name;

    private String avatar;

    private String phone;

    private String permissionWebsite;

    private String email;

    private String linkin;

    private String country;
}
