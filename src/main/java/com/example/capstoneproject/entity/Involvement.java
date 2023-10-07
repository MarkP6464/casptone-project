package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor

@Getter
@Setter
@Entity
public class Involvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String OrganizationRole;

    private String OrganizationName;

    private Date StartDate;

    private Date EndDate;

    private String College;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
