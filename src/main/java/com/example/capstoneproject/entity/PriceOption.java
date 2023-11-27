package com.example.capstoneproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class PriceOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer day;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Expert expert;
}
