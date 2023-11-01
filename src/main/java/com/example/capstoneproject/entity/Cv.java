package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.ScoreDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String resumeName;

    @Column(columnDefinition = "NVARCHAR(40)")
    private String fieldOrDomain;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String experience;

    private Boolean sharable;

    private Boolean searchable;

    @Column(columnDefinition = "TEXT")
    private String Summary;

    @Enumerated(EnumType.STRING)
    private BasicStatus status;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String cvBody;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String evaluation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_description_id")
    private JobDescription jobDescription;

    @OneToMany(fetch = FetchType.LAZY)
    private List<History> histories;

    public String toCvBody(CvBodyDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setCvBody(map);
        return map;
    }

    public CvBodyDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.cvBody, CvBodyDto.class);
    }

    public ScoreDto deserializeScore() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.evaluation, ScoreDto.class);
    }
}

