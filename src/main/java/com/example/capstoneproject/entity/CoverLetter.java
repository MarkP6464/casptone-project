package com.example.capstoneproject.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CoverLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String title;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String dear;

    private LocalDate date;
    @Column(columnDefinition = "NVARCHAR(100)")
    private String company;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;
    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coverLetter")
    private List<HistoryCoverLetter> historyCoverLetter;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
