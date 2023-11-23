package com.example.capstoneproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("HR")
public class HR extends Users{

    private Boolean subscription;

    @Column(name = "company_name", columnDefinition = "NVARCHAR(50)")
    private String companyName;

    @Column(name = "company_location", columnDefinition = "NVARCHAR(100)")
    private String companyLocation;

    private String companyLogo;

    @Column(name = "company_description", columnDefinition = "NVARCHAR(200)")
    private String companyDescription;

}
