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

    private String title;

    private String description;

    private Double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

}
