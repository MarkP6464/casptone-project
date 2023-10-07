package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor

@Getter
@Setter
@Entity
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String Name;

    private String CertificateSource;

    private int EndYear;

    private String CertificateRelevance;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
