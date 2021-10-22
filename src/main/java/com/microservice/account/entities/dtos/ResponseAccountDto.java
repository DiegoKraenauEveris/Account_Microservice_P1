package com.microservice.account.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountDto {
    private List<ResponseCustomerDto> customers;

    private String accountNumber;


}
