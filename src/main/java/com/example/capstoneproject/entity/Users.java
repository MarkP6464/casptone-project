package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.utils.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("customer")
@DiscriminatorColumn(name = "user_type")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String Name;

    private String Avatar;

    private String Address;

    private String Password;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    private String Phone;

    private String PermissionWebsite;

    private String Email;

    private String Linkin;

    private String Country;

    private String AccountBalance;

    private int Vip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "Id", insertable = false, updatable = false)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Certification> certifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Education> educations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Experience> experiences ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Involvement> involvements ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Project> projects ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<SourceWork> sourceWorks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Cv> cvs;

}
