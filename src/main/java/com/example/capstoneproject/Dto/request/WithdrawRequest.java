package com.example.capstoneproject.Dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
public class WithdrawRequest {
    @NotNull
    private Double expenditure;

    private Integer receiverId;
}
