package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.utils.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Map<String, Object> cvBody = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    public void toCvBody(){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(this, new TypeReference<Map<String, Object>>() {});
        this.setCvBody(map);
    }
}

