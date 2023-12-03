package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.JobDescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
public class CvUpdateDto {

    private String resumeName;

    private String jobTitle;

    private String companyName;

    private String jobDescriptionTarget;
}
