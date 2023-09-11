package com.example.capstoneproject.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class InvolvementOfCv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "involvement_id")
    private Involvement involvement;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
