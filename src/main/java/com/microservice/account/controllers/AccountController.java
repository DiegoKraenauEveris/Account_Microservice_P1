package com.microservice.account.controllers;

import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;
import com.microservice.account.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
