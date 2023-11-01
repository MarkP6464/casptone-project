package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CoverLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String title;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String dear;

    private LocalDate date;
    @Column(columnDefinition = "NVARCHAR(40)")
    private String company;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
