package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.SectionEvaluate;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SectionEvaluate TypeName;

    private Integer TypeId;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String Title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    private List<SectionLog> sectionLogs = new ArrayList<>();
}
