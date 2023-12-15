package com.example.capstoneproject.Dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HRBankRequest {

    private Integer id;

    private String bankName;

    private String bankAccountNumber;

    private String bankAccountName;

}