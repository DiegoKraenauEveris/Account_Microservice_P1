package com.microservice.account.services.impl;

import com.microservice.account.client.CustomerServiceClient;
import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;
import com.microservice.account.entities.dtos.ResponseCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements com.microservice.account.services.IAccountService {

    @Autowired
    private CustomerServiceClient customerClient;

    @Override
    public ResponseAccountDto createAccount(CreateAccountDto dto) throws Exception {

        //Create customer
        ResponseCustomerDto customer = customerClient.createCustomer(dto.getCustomer());

        log.info("Entroooooooooooo" + customer.get_id());
        return null;
    }
}
