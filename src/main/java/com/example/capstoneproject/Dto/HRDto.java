package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HRDto extends UsersDto {

    private Boolean subscription;

    private String companyName;

    private String companyLocation;

    private String companyLogo;

    private String companyDescription;

    private LocalDate expiredDay;
}
