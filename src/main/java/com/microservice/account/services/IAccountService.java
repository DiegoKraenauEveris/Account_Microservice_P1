package com.microservice.account.services;

import com.microservice.account.entities.Account;
import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;

import java.util.Optional;

public interface IAccountService {
    ResponseAccountDto createAccount(CreateAccountDto dto) throws  Exception;
    Account findAccountByAccountNumber(String accountNumber) throws  Exception;
}
