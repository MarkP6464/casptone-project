package com.example.capstoneproject.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class ApplicationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate timestamp;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String cv;

    @ManyToOne
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;
}
