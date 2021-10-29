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
    
    private boolean validatePersonalVipAccount(CreateAccountDto dto, List<ResponseCustomerDto> customers) throws Exception {
    	
    	if(customers.size() > 0) { // Si alguno de los clientes ya existe, verificar que tenga tarjeta de crédito
    		List<ObjectId> customersId = new ArrayList<>();
        	customers.forEach(customer -> { customersId.add(customer.get_id()); });
        	
        	List<ResponseAccountDto> result = findAccountByCustomersIdsIn(customersId).stream()
        				.filter(acc -> acc.getAccountType().getType().equals("TARJETA_CREDITO")).toList();
        		
        	if(result.size() > 0) {
        		return true;
        	} else {
        		log.info("Alguno de los clientes existentes no posee una tarjeta de crédito.");
        		return false;
        	}
        } else {
        	// Else si todos son nuevos, indicar que alguno se cree una tarjeta de credito primero
        	log.info("Crearse una tarjeta de crédito primero para acceder a la cuenta ahorro vip.");
        	return false;
        }
	}

    @Override
    public ResponseAccountDto createAccount(CreateAccountDto dto) throws Exception {
        List<ResponseCustomerDto> customers = new ArrayList<>();

        //Make dni array
        List<String> dnis = new ArrayList<>();
        boolean specialAccount = false;
        dto.getCustomers().forEach(customer->{
            dnis.add(customer.getDni());
        });
        
        //Customers that exists
        customers = customerClient.findCustomerByDni(dnis);
        
        // Si hay al menos un cliente personal_vip y se crea una cuenta tipo "ahorro"
        if(dto.getCustomers().stream().filter(customer -> customer.getType().getName().equals("PERSONAL_VIP")).count() > 0
        		&& dto.getAccount().getType().equals("AHORRO")) {
        	if(!validatePersonalVipAccount(dto, customers)) { // Si no tiene tarjeta de crédito  == false
        		throw new Exception("NO SE PUEDE CREAR CUENTA PERSONAL VIP."); 
        	}
        }; // Si tiene qué continue con la creación de la cuenta
        
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

        //Add new signers
        List<ResponseSignerDto> signers = new ArrayList<>();
        signers = customerClient.createSigners(dto.getSigners());

        //Gte signers ids after save
        List<ObjectId> signersIds = new ArrayList<>();
        signers.forEach(finalSigner->{
            signersIds.add(finalSigner.get_id());
        });
        
        //Create account
        Optional<AccountType> accTypeOptional = appConfig.getAccountTypeByName(dto.getAccount().getType());
        
        if(accTypeOptional.isPresent()){ ;
            AccountType accountType = accTypeOptional.get();
            Account account = Account.builder()
                    .balance(0.00)
                    .accountNumber(createRandomAccountNumber())
                    .customersIds(ids)
                    .signerIds(signersIds)
                    .accountType(accountType)
                    .build();
            account = accountRepository.save(account);

            ResponseAccountDto response = modelMapper.map(account,ResponseAccountDto.class);
            return  response;
        }else{
            throw new Exception("ACC_TYPE_NOT_FOUND");
        }
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
    public ConsultAccountDto consultAccount(String accountNumber) throws Exception {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()->new Exception("ACCOUNT_NOT_FOUND"));

        //Get transactions
        System.out.println(account.get_id().toString());
        List<TransactionDto> transactions = transactionClient.findTransactionsByAccountId(account.get_id().toString());
        ConsultAccountDto response = ConsultAccountDto.builder()
                .balance(account.getBalance())
                .transactions(transactions)
                .build();

        return  response;

    }

	@Override
	public List<ResponseAccountDto> findAccountByCustomersIdsIn(List<ObjectId> customersIds) throws Exception {
		List<Account> accounts = accountRepository.findAccountByCustomersIdsIn(customersIds);
		List<ResponseAccountDto> response = new ArrayList<>();
		accounts.forEach(account -> {
			response.add(modelMapper.map(account, ResponseAccountDto.class));
		});
//                .orElseThrow(() -> new Exception("ACCOUNT_NOT_FOUND"));
//        ResponseAccountDto response =  modelMapper.map(account,ResponseAccountDto.class);
//        return response;
		return response;
	}
	
	
}
