package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyReviewDto;
import com.example.capstoneproject.enums.StatusReview;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class ReviewResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_response_id")
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String overall;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String feedbackDetail;

    private Double score;

    private LocalDate dateComment;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    private StatusReview status;

    private LocalDateTime dateDone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_request_id")
    private ReviewRequest reviewRequest;

    public String toCvBodyReview(CvBodyReviewDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setFeedbackDetail(map);
        return map;
    }

    public CvBodyReviewDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.feedbackDetail, CvBodyReviewDto.class);
    }


}
