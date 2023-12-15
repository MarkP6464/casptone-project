package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class UsersCvViewDto {
    private String fullName;

    private String phone;

    private String personalWebsite;

    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Please check your email format!!")    private String email;
    private String linkin;

    private String city;
}
