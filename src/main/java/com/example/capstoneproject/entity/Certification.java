package com.example.capstoneproject.entity;
import com.example.capstoneproject.enums.BasicStatus;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String Name;

    @Column(name = "certificate_source")
    private String CertificateSource;

    @Column(name = "end_year")
    private int EndYear;

    @Column(name = "certificate_relevance")
    private String CertificateRelevance;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
