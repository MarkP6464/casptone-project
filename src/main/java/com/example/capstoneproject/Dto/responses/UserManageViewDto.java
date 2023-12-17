package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserManageViewDto {
    private Integer id;

    private String name;

    private String status;

    private String money;

    private LocalDate lastActive;

    private RoleType role;
}
