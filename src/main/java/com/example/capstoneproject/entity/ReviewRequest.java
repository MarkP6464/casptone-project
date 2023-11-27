package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.StatusReview;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReviewRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_request_id")
    private Integer id;

    private Timestamp receivedDate;

    private Timestamp deadline;

    private Long price;

    @Enumerated(EnumType.STRING)
    private StatusReview status;

    @Column(columnDefinition = "TEXT")
    private String note;

    private Integer expertId;

    private Integer historyId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
