package com.microservice.account.client;

import com.microservice.account.config.AccountServiceConfig;
import com.microservice.account.entities.dtos.CustomerDto;
import com.microservice.account.entities.dtos.ResponseCustomerDto;
import com.microservice.account.entities.dtos.ResponseSignerDto;
import com.microservice.account.entities.dtos.SignerDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CustomerServiceClient {

    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    WebClient.Builder client;

    @Autowired
    private AccountServiceConfig config;

//    @Autowired
//    private CircuitBreakerFactory circuitBreakerFactory;

    public List<ResponseCustomerDto> createCustomers(List<CustomerDto> dtos){
    	
        List<ResponseCustomerDto> result = new ArrayList<>();
        
        //Heders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Content
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(dtos,headers);
        
//        CircuitBreaker cb = circuitBreakerFactory.create("clientservicebreaker");
//        
//        return cb.run(() -> restTemplate.exchange(config.getCustomerServiceUrl()+"/createCustomers",
//              HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseCustomerDto>>() {
//              }).getBody(), throwable -> fallbackClientService());
        result =  restTemplate.exchange(config.getCustomerServiceUrl()+"/createCustomers",
                HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseCustomerDto>>() {
                }).getBody();
        
        log.info("Response:" + requestEntity.getHeaders());
        return result;
    }
    
    public List<ResponseSignerDto> createSigners(List<SignerDto> dtos){
        List<ResponseSignerDto> result = new ArrayList<>();
        //Heders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Content
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(dtos,headers);
        
//        CircuitBreaker cb = circuitBreakerFactory.create("clientsignersservicebreaker");
//        return cb.run(() -> restTemplate.exchange(config.getCustomerServiceUrl()+"/createSigners",
//                HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseSignerDto>>() {
//                }).getBody(), throwable -> fallbackClientSignersService());
        
        result = restTemplate.exchange(config.getCustomerServiceUrl()+"/createSigners",
                HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseSignerDto>>() {
                }).getBody();
        
        log.info("Response:" + requestEntity.getHeaders());
        return result;
    }


    public List<ResponseCustomerDto> findCustomerByDni(List<String> dnis){
        List<ResponseCustomerDto> result = new ArrayList<>();
        try{
            //Heders
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //Content
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(dnis,headers);
            
            
            
//            CircuitBreaker cb = circuitBreakerFactory.create("findcustomercircuitbreaker");
//            return cb.run(() -> restTemplate.exchange(config.getCustomerServiceUrl()+"/findCustomers",
//                       HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseCustomerDto>>() {
//            }).getBody(), throwable -> fallbackClientService());
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
    
    public List<ResponseCustomerDto> fallbackClientService() {
    	List<ResponseCustomerDto> response = new ArrayList<>();
    	log.info("El servicio de Customer no se encuentra disponible");
    	return response;
    }
    
    public List<ResponseSignerDto> fallbackClientSignersService() {
    	List<ResponseSignerDto> response = new ArrayList<>();
    	log.info("El servicio de Customer no se encuentra disponible");
    	return response;
    }

}
