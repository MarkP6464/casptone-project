package com.example.capstoneproject.entity;
import com.example.capstoneproject.enums.BasicStatus;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String Name;

    private int AmountView;

    private String Content;

    @Enumerated(EnumType.STRING)
    private BasicStatus Status;
}
