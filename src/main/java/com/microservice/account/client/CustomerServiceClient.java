package com.microservice.account.client;

import com.microservice.account.config.AccountServiceConfig;
import com.microservice.account.entities.dtos.CustomerDto;
import com.microservice.account.entities.dtos.ResponseCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CustomerServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private AccountServiceConfig config;


    public ResponseCustomerDto createCustomer(CustomerDto dto){
        ResponseEntity<ResponseCustomerDto> responseCustomer = restTemplate.postForEntity(config.getCustomerServiceUrl(),dto,ResponseCustomerDto.class);
        log.info("Response:" + responseCustomer.getHeaders());
        return responseCustomer.getBody();
    }


}
