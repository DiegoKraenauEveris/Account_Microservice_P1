package com.microservice.account.services.impl;

import com.microservice.account.client.CustomerServiceClient;
import com.microservice.account.config.AppConfig;
import com.microservice.account.entities.Account;
import com.microservice.account.entities.AccountType;
import com.microservice.account.entities.dtos.CreateAccountDto;
import com.microservice.account.entities.dtos.CustomerDto;
import com.microservice.account.entities.dtos.ResponseAccountDto;
import com.microservice.account.entities.dtos.ResponseCustomerDto;
import com.microservice.account.repositories.IAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class AccountServiceImpl implements com.microservice.account.services.IAccountService {

    @Autowired
    private CustomerServiceClient customerClient;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private  AppConfig appConfig;

    private String createRandomAccountNumber(){
        Random rand = new Random();
        StringBuilder card = new StringBuilder();
        for (int i = 0; i < 20; i++)
        {
            int n = rand.nextInt(10);
            card.append(Integer.toString(n));
        }
        return card.toString();
    }

    @Override
    public ResponseAccountDto createAccount(CreateAccountDto dto) throws Exception {
        List<ResponseCustomerDto> customers = new ArrayList<>();

        //Make dni array
        List<String> dnis = new ArrayList<>();
        dto.getCustomers().forEach(customer->{
            dnis.add(customer.getDni());
        });

        //Customers that exists
        customers = customerClient.findCustomerByDni(dnis);

        //Clear dnis and add only dnis of customers found
        dnis.clear();
        customers.forEach(customer->{
            dnis.add(customer.getDni());
        });

        //Add new customers
        List<CustomerDto> customersMissing = new ArrayList<>();
        dto.getCustomers().forEach(customer->{
            if(!dnis.contains(customer.getDni())){
                customersMissing.add(customer);
            }
        });
        customers.addAll(customerClient.createCustomers(customersMissing));

        //Get customers ids after save
        List<ObjectId> ids = new ArrayList<>();
        customers.forEach(finalCustomer->{
            ids.add(finalCustomer.get_id());
        });

        //Create account
        Optional<AccountType> accTypeOptional = appConfig.getAccountTypeByName(dto.getAccount().getType());

        if(accTypeOptional.isPresent()){ ;
            AccountType accountType = accTypeOptional.get();
            Account account = Account.builder()
                    .balance(0.00)
                    .accountNumber(createRandomAccountNumber())
                    .customersIds(ids)
                    .accountType(accountType)
                    .build();
            account = accountRepository.save(account);
        }else{
            throw new Exception("ACC_TYPE_NOT_FOUND");
        }

        return null;
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) throws Exception {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("ACCOUNT_NOT_FOUND"));
        System.out.println(account.getCustomersIds().get(0));
        return account;
    }
}
