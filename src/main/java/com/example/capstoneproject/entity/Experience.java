package com.example.capstoneproject.entity;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Role;

    private String CompanyName;

    private Date StartDate;

    private Date EndDate;

    private String Location;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Enumerated(EnumType.ORDINAL)
    private CvStatus Status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL)
    private List<Evaluate> evaluates;

}
