package com.example.capstoneproject.entity;

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
    private String feedbackDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_review_request_id")
    private ReviewRequest reviewRequest;
}
