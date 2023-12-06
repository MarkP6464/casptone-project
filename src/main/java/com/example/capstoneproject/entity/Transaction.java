package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter @Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentId;

    @Column(unique = true)
    private String requestId;

    private String momoId;

    @Column(columnDefinition = "TEXT")
    private String responseMessage;

    private TransactionType transactionType;

    private MoneyType moneyType;

    private Long expenditure;

    private Long conversionAmount;

    private Long fee;

    private TransactionStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Users user;

    public Transaction(Long id, String sentId, String requestId, String momoId, String responseMessage, TransactionType transactionType, MoneyType moneyType, Long expenditure, Long conversionAmount, Long fee, TransactionStatus status, Users user) {
        this.id = id;
        this.sentId = sentId;
        this.requestId = requestId;
        this.momoId = momoId;
        this.responseMessage = responseMessage;
        this.moneyType = moneyType;
        this.transactionType = transactionType;
        this.expenditure = expenditure;
        this.conversionAmount = conversionAmount;
        this.fee = fee;
        this.status = status;
        this.user = user;
    }
}
