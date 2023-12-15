package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.mservice.allinone.models.QueryStatusTransactionResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class AddMoneyTransactionDto {
    QueryStatusTransactionResponse momoResponse;

    Double conversionAmount;
}
