package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.SectionLogStatus;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SectionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String Bullet;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private SectionLogStatus Status;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "evaluate_id")
    private Evaluate evaluate;
}
