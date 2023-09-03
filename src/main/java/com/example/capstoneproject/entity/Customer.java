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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "Password")
    private String password;

    @Column(name = "Status")
    private int status;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Permission_Website")
    private String permissionWebsite;

    @Column(name = "Email")
    private String email;

    @Column(name = "Linkin")
    private String linkin;

    @Column(name = "Country")
    private String country;

    @Column(name = "Account_Balance")
    private String accountBalance;

    @Column(name = "Vip")
    private int vip;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

}
