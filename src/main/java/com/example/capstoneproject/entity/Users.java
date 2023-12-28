package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorFormula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter @Setter
@DiscriminatorFormula(
        "CASE WHEN price IS NOT NULL THEN 'Expert' " +
                " WHEN publish IS NOT NULL THEN 'Candidate' " +
                " WHEN expired_day IS NOT NULL THEN 'HR' " +
                "WHEN configuration IS NOT NULL THEN 'Admin' " +
                "ELSE 'Users' end"
)
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "NVARCHAR(50)")
    @NotNull
    private String Name;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String address;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String jobTitle;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String company;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(name = "password",columnDefinition = "VARCHAR(20)")
    private String password;

    @Column(name = "status", columnDefinition = "NVARCHAR(30)")
    @Enumerated(EnumType.STRING)
    private BasicStatus status;

    @Column(name = "phone", columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(name = "personal_Website", columnDefinition = "TEXT")
    private String personalWebsite;

    @Column(name = "email", columnDefinition = "NVARCHAR(50)")
    private String email;

    @Column(name = "linkin", columnDefinition = "TEXT")
    private String linkin;

    @Column(name = "country", columnDefinition = "NVARCHAR(50)")
    private String country;

    @Column(name = "account_Balance")
    private Double accountBalance = 0.0;

    private LocalDate createDate;

    private LocalDate lastActive;

    private boolean ban;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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

}
