package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Content;

    @Column(columnDefinition = "TEXT")
    private String Summary;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Certification> certifications = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Involvement> involvements = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<SourceWork> sourceWorks = new ArrayList<>();

}
