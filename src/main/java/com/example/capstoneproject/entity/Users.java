package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String Name;

    private String address;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BasicStatus status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "personal_Website")
    private String personalWebsite;

    @Column(name = "email")
    private String email;

    @Column(name = "linkin")
    private String linkin;

    @Column(name = "country")
    private String country;

    @Column(name = "account_Balance")
    private String accountBalance;

    @Column(name = "vip")
    private Integer vip;

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
    private List<Experience> experiences;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Involvement> involvements;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Project> projects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Cv> cvs;

}
