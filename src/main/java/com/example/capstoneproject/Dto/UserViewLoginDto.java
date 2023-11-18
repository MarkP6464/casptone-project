package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Role;
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

    private String permissionWebsite;

    private String email;

    private String linkin;

    private RoleDto role;
}
