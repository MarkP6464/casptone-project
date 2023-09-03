package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private String summary;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
