package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Expert{

    @Id
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String title;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String company;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    private Integer numberReview;

    private Integer experience;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

}
