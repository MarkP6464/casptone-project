package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<Users> user;
}
