package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExperienceDto {
    private int id;

    private String RoleCompany;

    private String Name;

    private Date StartDate;

    private Date EndDate;

    private String Location;

    private String Description;

    private CvStatus Status;

    private CvAddDto cv;
}
