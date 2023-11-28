package com.example.capstoneproject.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class HistoryCoverLetter {
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
    @ManyToOne
    @JoinColumn(name = "cover_letter_id")
    private CoverLetter coverLetter;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
