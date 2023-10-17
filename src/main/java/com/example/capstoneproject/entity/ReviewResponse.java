package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.CvBodyReviewDto;
import com.example.capstoneproject.enums.ReviewStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ReviewResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String overall;

    @Column(columnDefinition = "TEXT")
    private String feedbackDetail = "{\"skills\":[],\"certifications\":[],\"educations\":[],\"experiences\":[],\"involvements\":[],\"projects\":[],\"sourceWorks\":[]}";

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_review_request_id")
    private ReviewRequest reviewRequest;

    public void toCvBodyReview(CvBodyReviewDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setFeedbackDetail(map);
    }

    public CvBodyReviewDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.feedbackDetail, CvBodyReviewDto.class);
    }
}
