package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Involvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String OrganizationRole;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String OrganizationName;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String duration;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String College;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private Cv cv;

}
