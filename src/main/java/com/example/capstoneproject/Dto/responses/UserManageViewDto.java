package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.RoleDto;
import com.example.capstoneproject.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserManageViewDto {
    private Integer id;

    private String name;

    private String avatar;

    private String phone;

    private String email;

    private Long accountBalance;

    private RoleType role;
}
