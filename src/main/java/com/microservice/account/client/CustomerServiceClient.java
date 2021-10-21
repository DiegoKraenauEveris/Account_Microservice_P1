package com.microservice.account.client;

import com.microservice.account.config.AccountServiceConfig;
import com.microservice.account.entities.dtos.CustomerDto;
import com.microservice.account.entities.dtos.ResponseCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CustomerServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private AccountServiceConfig config;


    public ResponseCustomerDto createCustomer(CustomerDto dto){
        System.out.println(dto);
        ResponseEntity<ResponseCustomerDto> responseCustomer = restTemplate.postForEntity(config.getCustomerServiceUrl(),dto,ResponseCustomerDto.class);
        log.info("Response:" + responseCustomer.getHeaders());
        return responseCustomer.getBody();
    }

    public List<ResponseCustomerDto> findCustomerByDni(List<String> dnis){
        List<ResponseCustomerDto> result = new ArrayList<>();
        try{
            //Heders
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //Content
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(dnis,headers);
            result =  restTemplate.exchange(config.getCustomerServiceUrl()+"/findCustomers",
                       HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseCustomerDto>>() {
            }).getBody();
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode() != HttpStatus.NOT_FOUND){
                throw  ex;
            }
        }
        return result;
    }


}
