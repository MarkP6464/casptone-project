package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDateTime receivedDate;

    private LocalDateTime deadline;

    private Double price;

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
