package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Role;
import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    private String Name;

    private String address;

    private String avatar;

    private String password;

    @Enumerated(EnumType.STRING)
    private BasicStatus status;

    private String phone;

    private Long accountBalance = 0L;

    private Long quota = 0L;

    private String personalWebsite;

    private String email;

    private String linkin;

    private String country;

    private Role role;
}
