package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.utils.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    @NotNull
    private String Name;

    @Column(name = "Avatar")
    private String Avatar;

    @Column(name = "Password")
    private String Password;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @Column(name = "Phone")
    private String Phone;

    @Column(name = "Permission_Website")
    private String PermissionWebsite;

    @Column(name = "Email")
    private String Email;

    @Column(name = "Linkin")
    private String Linkin;

    @Column(name = "Country")
    private String Country;

    @Column(name = "Account_Balance")
    private String AccountBalance;

    @Column(name = "Vip")
    private int Vip;

    @ManyToOne
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

}
