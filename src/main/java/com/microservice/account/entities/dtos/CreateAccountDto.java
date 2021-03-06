package com.microservice.account.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDto {

    private List<CustomerDto> customers;

    private AccountDto account;

    private List<SignerDto> signers;
}
