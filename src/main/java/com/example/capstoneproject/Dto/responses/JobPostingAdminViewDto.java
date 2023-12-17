package com.example.capstoneproject.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingAdminViewDto {
    private Integer id;
    private String jobTitle;
    private String company;
    private String owner;
    private String status;
    private LocalDateTime createDate;
}
