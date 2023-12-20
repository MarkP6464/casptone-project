package com.example.capstoneproject.entity;

import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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

    private Double expenditure;

    private Double conversionAmount;

    private Long fee;

    private TransactionStatus status;

    private LocalDateTime createdDate = LocalDateTime.now().plusHours(7);

    private LocalDateTime updateDate = LocalDateTime.now().plusHours(7);

    @Column(columnDefinition = "TEXT")
    private String proof;

    @Column(name = "bank_name", columnDefinition = "NVARCHAR(20)")
    private String bankName;

    @Column(name = "bank_account_name", columnDefinition = "NVARCHAR(20)")
    private String bankAccountName;

    @Column(name = "bank_account_number", columnDefinition = "NVARCHAR(20)")
    private String bankAccountNumber;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Users user;

    public Transaction(Long id, String sentId, String requestId, String momoId, String responseMessage, TransactionType transactionType, MoneyType moneyType, Double expenditure, Double conversionAmount, Long fee, TransactionStatus status, Users user) {
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
