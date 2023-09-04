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
public class InvolvementDto {
    private int id;

    private String OrganizationRole;

    private String OrganizationName;

    private Date StartDate;

    private Date EndDate;

    private String CollegeLocation;

    private String Description;

    private CvStatus Status;

    private Cv cv;
}
