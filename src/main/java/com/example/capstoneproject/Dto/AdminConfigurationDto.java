package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class AdminConfigurationDto{
    private Integer id;

    public Long vipMonthRatio;

    public Long vipYearRatio;
}
