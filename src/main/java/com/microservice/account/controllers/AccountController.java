package com.microservice.account.controllers;

import com.microservice.account.entities.Account;
import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;
import com.microservice.account.entities.dtos.TransactionDto;
import com.microservice.account.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE })
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    IAccountService accountService;

    @PostMapping()
    public ResponseAccountDto createAccount(@Validated @RequestBody CreateAccountDto dto) throws  Exception{
        return accountService.createAccount(dto);
    }

    @GetMapping(value = "findByAccountNumber/{accountNumber}")
    public ResponseAccountDto findAccountByAccountNumber(@PathVariable String accountNumber) throws  Exception{
        return accountService.findAccountByAccountNumber(accountNumber);
    }

    @PutMapping(value = "updateAmount/{accountId}")
    public ResponseAccountDto updateAmount(@Validated @RequestBody TransactionDto dto, @PathVariable String accountId) throws  Exception{
        return accountService.updateAmount(dto,accountId);
    }

    @GetMapping("consultAccountByAccountNumber/{accountNumber}")
    public List<TransactionDto> consultAccount(@PathVariable String accountNumber) throws  Exception{
        return accountService.consultAccount(accountNumber);
    }

}
