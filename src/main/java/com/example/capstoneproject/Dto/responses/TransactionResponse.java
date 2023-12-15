package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
public class TransactionResponse {
    @NotNull
    private Double expenditure;

    private Double conversionAmount;

    private Integer userId;

    private String sentId;
}
