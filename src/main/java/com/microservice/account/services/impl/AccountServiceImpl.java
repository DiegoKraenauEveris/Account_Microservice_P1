package com.microservice.account.services.impl;

import com.microservice.account.client.CustomerServiceClient;
import com.microservice.account.client.TransactionServiceClient;
import com.microservice.account.config.AppConfig;
import com.microservice.account.entities.Account;
import com.microservice.account.entities.AccountType;
import com.microservice.account.entities.dtos.*;
import com.microservice.account.repositories.IAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private TransactionServiceClient transactionClient;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private  AppConfig appConfig;

    public static final ModelMapper modelMapper=new ModelMapper();

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
    public ResponseAccountDto findAccountByAccountNumber(String accountNumber) throws Exception {

        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("ACCOUNT_NOT_FOUND"));
        ResponseAccountDto response =  modelMapper.map(account,ResponseAccountDto.class);
        return response;
    }

    @Override
    public ResponseAccountDto updateAmount(TransactionDto dto, String accountId) throws Exception {
        //Validate that account exists
        Account account = accountRepository.findById(new ObjectId(accountId))
                .orElseThrow(()->new Exception("ACCOUNT_NOT_FOUND"));
        //Update amount
        switch (dto.getTransactionType()){
            case "DEPOSITO":
                account.setBalance(account.getBalance() + dto.getAmount());
                accountRepository.save(account);
                break;
            case "RETIRO":
                account.setBalance(account.getBalance() - dto.getAmount());
                accountRepository.save(account);
                break;
        }

        ResponseAccountDto response = modelMapper.map(account,ResponseAccountDto.class);

        return response;
    }

    @Override
    @Transactional
    public List<TransactionDto> consultAccount(String accountNumber) throws Exception {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()->new Exception("ACCOUNT_NOT_FOUND"));

        //Get transactions
        List<TransactionDto> transactions = transactionClient.findTransactionsByAccountId(account.get_id().toString());

        return  transactions;

    }
}
