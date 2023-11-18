package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private Integer Id;

    private RoleType roleName;

}
