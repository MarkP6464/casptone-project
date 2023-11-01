package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogViewDto {
    private Integer id;

    private String title;

    private String workingType;

    private String location;

    private String description;

    private String requirement;

    private Integer salary;

    private LocalDate deadline;

    private LocalDate createDate;

    private LocalDate updateDate;

    private BasicStatus status;

    private BasicStatus share;

    private List<CvViewDto> cvs;

}
