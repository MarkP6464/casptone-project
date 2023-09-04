package com.example.capstoneproject.entity;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String FullName;

    private String Phone;

    private Date Website;

    private String State;

    private String Email;

    private Date Linkin;

    private String Country;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @OneToOne(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cv cv;
}
