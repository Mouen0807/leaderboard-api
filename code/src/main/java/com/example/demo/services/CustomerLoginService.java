package com.example.demo.services;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.mappers.CustomerLoginMapperImpl;
import com.example.demo.repositories.CustomerLoginRepository;
import com.example.demo.models.CustomerLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerLoginService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginService.class);
    private final CustomerLoginRepository customerLoginRepository;
    private final CustomerLoginMapperImpl customerLoginMapperImpl = new CustomerLoginMapperImpl();
    @Autowired
    private PasswordEncoder passwordEncoder;


    public CustomerLoginService(CustomerLoginRepository customerLoginRepository) {
        this.customerLoginRepository = customerLoginRepository;
    }

    public Optional<CustomerLoginDto> createCustomerLogin(CustomerLogin customerLogin){
        try {
            logger.debug("Start saving customer with login: {} ", customerLogin.getLogin());
            Optional<CustomerLogin> optCustomerLogin = Optional.ofNullable(customerLoginRepository.findByLogin(customerLogin.getLogin()));
            
            if(!optCustomerLogin.isPresent()){
                String customerPassword = customerLogin.getPassword();
                customerLogin.setPassword(passwordEncoder.encode(customerPassword));
                CustomerLogin customerSaved = customerLoginRepository.save(customerLogin);

                logger.debug("Customer is saved");
                return Optional.of(customerLoginMapperImpl.convertToDto(customerSaved));
            }else{
                logger.debug("Customer login already exist");
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Failed to save customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerLoginDto> fetchCustomerLoginByLogin(String login) {
        try {
            logger.debug("Start retrieving customer with login: {} ", login);
            Optional<CustomerLogin> customerLoginFound = Optional.ofNullable(customerLoginRepository.findByLogin(login));

            if(customerLoginFound.isPresent()){
                CustomerLoginDto customerLoginDtoFound  = customerLoginMapperImpl.convertToDto(customerLoginFound.get());
                logger.debug("Customer successfully retrieved");
                return Optional.of(customerLoginDtoFound);
            }else{
                logger.debug("Customer not found");
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerLoginDto> verifyCustomerLogin(String login, String rawPassword){
        try {
            logger.debug("Start to log customer with login {} ", login);
            Optional<CustomerLogin> customerLoginFound = Optional.ofNullable(customerLoginRepository.findByLogin(login));

            if(customerLoginFound.isPresent()){
                if(passwordEncoder.matches(rawPassword, customerLoginFound.get().getPassword())){
                    Optional<CustomerLoginDto> customerLoginDtoFound = Optional.of(
                            customerLoginMapperImpl.convertToDto(customerLoginFound.get()));

                    logger.debug("Customer is logged successfully");
                    return customerLoginDtoFound;
                }
            }

            logger.debug("Customer is not logged ");
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Failed to log customer");
            throw new RuntimeException(e.getMessage());
        }
    }
}
