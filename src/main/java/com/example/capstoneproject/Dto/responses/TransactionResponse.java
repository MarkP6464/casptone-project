package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
public class TransactionResponse {
    @NotNull
    private Long expenditure;

    private Long conversionAmount;

    @NotNull
    private Integer userId;

    private String sentId;
}
