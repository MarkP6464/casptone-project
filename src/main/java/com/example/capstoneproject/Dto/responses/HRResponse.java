package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.UsersDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HRResponse extends UsersDto {

    private String companyName;

    private String companyLocation;

    private String companyLogo;

    private String companyDescription;

}
