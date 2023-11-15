package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRequestDto {
    private Integer id;

    private LocalDate deadline;

    private Double price;

    private StatusReview status;

    private String note;

    private Integer expertId;

    private Integer historyId;

    private Cv cv;
}
