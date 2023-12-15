package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.ApplicationLogStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(columnDefinition = "TEXT")
    private String Note;

    private Integer cv;

    private Integer coverLetter;

    @Enumerated(EnumType.STRING)
    private ApplicationLogStatus status;

    @ManyToOne
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
