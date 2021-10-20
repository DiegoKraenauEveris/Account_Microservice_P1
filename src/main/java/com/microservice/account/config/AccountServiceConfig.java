package com.microservice.account.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.modelmapper.ModelMapper;

@Getter
@Configuration
@PropertySource({"classpath:application.properties"})
public class AccountServiceConfig {

    @Value("${customerservice.url}")
    private String customerServiceUrl;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
