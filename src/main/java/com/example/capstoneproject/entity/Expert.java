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

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

}
