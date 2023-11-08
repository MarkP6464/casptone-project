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
    private Integer id;

    @Column(name = "name", columnDefinition = "NVARCHAR(100)")
    @NotNull
    private String Name;

    @Column(name = "certificate_source", columnDefinition = "NVARCHAR(100)")
    private String CertificateSource;

    @Column(name = "end_year")
    private Integer EndYear;

    @Column(name = "certificate_relevance", columnDefinition = "NVARCHAR(100)")
    private String CertificateRelevance;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
