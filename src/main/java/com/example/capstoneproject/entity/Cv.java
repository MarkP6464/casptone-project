package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyDto;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String Content;

    @Column(columnDefinition = "TEXT")
    private String Summary;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

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

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_description_id")
    private JobDescription jobDescription;

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
}

