package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users implements UserDetails {
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
    private Long accountBalance = 0L;

    @Column(name = "quota")
    private Long quota = 0L;

    @Column(name = "vip")
    private Boolean vip;

    @ManyToOne
    @JoinColumn(name = "role_id")
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

//    @OneToOne(mappedBy = "expert")
//    private Expert expert;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private void minusQuota(){
        this.setQuota(this.getQuota() - 1);
    }
}
