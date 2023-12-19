package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class TransactionViewDto {
    private Long id;

    private String sentId;

    private String requestId;

    private String momoId;

    private String responseMessage;

    private TransactionType transactionType;

    private MoneyType moneyType;

    private Double expenditure;

    private Double conversionAmount;

    private String proof;

    private TransactionStatus status;

    private Integer userId;

    private LocalDateTime createdDate;

    private String bankAccountName;

    private String bankName;

    private String bankAccountNumber;
}
