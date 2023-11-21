package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("Expert")
public class Expert extends Users{

    private Boolean punish = false;

    private LocalDate punishDate;

    @NonNull
    private Double price = 0.0;

    private Integer numberReview;

    private Integer experience;

    private Integer historyId;

}
