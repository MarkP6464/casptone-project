package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserViewLoginDto {
    private Integer id;

    private String name;

    private String avatar;

    private String phone;

    private String personalWebsite;

    private String email;

    private String linkin;

    private Double accountBalance;

    private boolean isBanned;

    private Boolean subscription;

    private Boolean vip;

    private RoleDto role;
}
