package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "RolePermission")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "RoleId")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "PermissionId")
    private Permission permission;
}
