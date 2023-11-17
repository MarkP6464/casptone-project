package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@NoArgsConstructor
@Getter @Setter
public class TransactionDto {
    private String sentId;

    private String requestId;

    private String momoId;

    private String responseMessage;

    private TransactionType transactionType;

    private MoneyType moneyType;

    private Long expenditure;

    private Long conversionAmount;

    private TransactionStatus status;

    private Integer userId;
}