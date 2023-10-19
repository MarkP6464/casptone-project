package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("expert")
@Table(name = "expert")
@Entity
public class Expert extends Users{

    private String title;

    private String description;

    private Double price;

}
