package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.SectionLogStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
