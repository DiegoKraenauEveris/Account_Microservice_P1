package com.microservice.account.services;

import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;

public interface AccountService {
    ResponseAccountDto createAccount(CreateAccountDto dto) throws  Exception;
}
