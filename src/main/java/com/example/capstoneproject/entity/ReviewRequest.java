package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.ReviewStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ReviewRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_request_id")
    private Integer id;

    private Date receivedDate;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(columnDefinition = "TEXT")
    private String note;

    private Integer expertId;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
