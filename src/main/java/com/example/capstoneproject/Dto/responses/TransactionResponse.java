package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class TransactionResponse {
    private String sentId;

    private Long expenditure;

    private Long conversionAmount;

    private Integer userId;
}
