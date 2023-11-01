package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.JobDescription;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class CvDupDto {
    private Integer id;

    private String resumeName;

    private String fieldOrDomain;

    private String experience;

    private String Summary;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @Type(type = "json")
    private String cvBody;

    @Type(type = "json")
    private String evaluation;

    private Users user;

    private JobDescription jobDescription;
}
