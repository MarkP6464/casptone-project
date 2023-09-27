package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.SectionEvaluate;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Section {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private SectionEvaluate TypeName;
    private int TypeId;
    private String Title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    private List<SectionLog> sectionLogs = new ArrayList<>();
}
