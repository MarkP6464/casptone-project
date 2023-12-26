package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class JobDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_description_id")
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ats")
    private List<Ats> ats;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")  // Assuming you have a corresponding column in your JobDescription table
    private JobPosting jobPosting;
}
