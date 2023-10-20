package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingDto {

    private String title;

    private String workingType;

    private String location;

    private String description;

    private String requirement;

    private Integer salary;

}
