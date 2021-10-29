package com.microservice.account.services;

import com.microservice.account.entities.Account;
import com.microservice.account.entities.dtos.ConsultAccountDto;
import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;
import com.microservice.account.entities.dtos.TransactionDto;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

public interface IAccountService {
    ResponseAccountDto createAccount(CreateAccountDto dto) throws  Exception;
    ResponseAccountDto findAccountByAccountNumber(String accountNumber) throws  Exception;
    List<ResponseAccountDto> findAccountByCustomersIdsIn(List<ObjectId> customersIds) throws Exception;
    ResponseAccountDto updateAmount(TransactionDto dto,String accountId) throws  Exception;
    ConsultAccountDto consultAccount(String accountNumber) throws  Exception;
}
