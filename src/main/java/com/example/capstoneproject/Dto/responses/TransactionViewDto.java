package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;

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

    private UsersDto receiver;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private HRBankRequest bank;
}
