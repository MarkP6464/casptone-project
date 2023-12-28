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

    @Column(columnDefinition = "NVARCHAR(100)")
    private String resumeName;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String companyName;

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
    private String overview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_description_id")
    private JobDescription jobDescription;

    @OneToOne(mappedBy = "cv")
    private Score score;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Certification> certifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Education> educations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Experience> experiences;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Involvement> involvements;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv")
    private List<Project> projects;

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

    public String toOverviewBody(ScoreDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setOverview(map);
        return map;
    }

    public ScoreDto deserializeOverview() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.overview, ScoreDto.class);
    }

    private Cv updateCvFromScoreDto(Cv cv, ScoreDto scoreDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String overviewMap = objectMapper.writeValueAsString(scoreDto);
        cv.setOverview(overviewMap);
        return cv;
    }

//    public ScoreDto deserializeScore() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(this.evaluation, ScoreDto.class);
//    }
}

