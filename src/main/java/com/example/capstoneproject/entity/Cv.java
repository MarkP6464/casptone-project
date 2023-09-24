package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.utils.HashMapConverter;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

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

    private String Content;

    @Column(columnDefinition = "TEXT")
    private String Summary;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Cv> cvBody;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
