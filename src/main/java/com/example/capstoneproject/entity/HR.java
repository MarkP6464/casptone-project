package com.example.capstoneproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("HR")
public class HR extends Users{

    private Boolean subscription = false;

    @Column(name = "company_name", columnDefinition = "NVARCHAR(50)")
    @NotNull
    private String companyName;

    @Column(name = "company_location", columnDefinition = "NVARCHAR(100)")
    @NotNull
    private String companyLocation;

    @Column(columnDefinition = "TEXT")
    private String companyLogo;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Column(columnDefinition = "TEXT")
    private String unJob;

    @Column(name = "vip")
    private Boolean vip = false;

    @NonNull
    private LocalDate expiredDay = LocalDate.now();

}
