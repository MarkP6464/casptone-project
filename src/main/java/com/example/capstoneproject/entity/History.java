package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyReviewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String cvBody;

    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private Cv cv;

    public String toHistoryCvBody(CvBodyReviewDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setCvBody(map);
        return map;
    }

    public CvBodyReviewDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.cvBody, CvBodyReviewDto.class);
    }
}
