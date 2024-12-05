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
            logger.debug("start saving customer with login: {} ", customerLogin.getLogin());

            Optional<CustomerLogin> optCustomerLogin = Optional.ofNullable(customerLoginRepository.findByLogin(customerLogin.getLogin()));
            
            if(!optCustomerLogin.isPresent()){
                String customerPassword = customerLogin.getPassword();
                customerLogin.setPassword(passwordEncoder.encode(customerPassword));

                CustomerLogin customerSaved = customerLoginRepository.save(customerLogin);

                logger.debug("customer saved with id: {} ", customerLogin.getId());
                
                return Optional.of(customerLoginMapperImpl.convertToDto(customerSaved));
            }else{
                logger.debug("customer with login: {} already exist", customerLogin.getLogin());
                
                return Optional.empty();
            }


        } catch (Exception e) {

            logger.error("Failed to save customer with login: {}", customerLogin.getLogin());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerLoginDto> fetchCustomerLoginByLogin(String login) {
        try {
            logger.debug("start retrieving customer login for login: {} ", login);

            Optional<CustomerLogin> customerLoginFound = Optional.ofNullable(customerLoginRepository.findByLogin(login));

            if(customerLoginFound.isPresent()){
                CustomerLoginDto customerLoginDtoFound  = customerLoginMapperImpl.convertToDto(customerLoginFound.get());
                
                logger.debug("Customer login {} successfully retrieved", login);
                
                return Optional.of(customerLoginDtoFound);
                
            }

            logger.debug("Customer {} not found ", login);

            return Optional.empty();

        } catch (Exception e) {
            logger.error("Failed to retrieve customer with login: {}", login);
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerLogin> fetchCustomerByLogin(String login) {
        try {
            return Optional.ofNullable(customerLoginRepository.findByLogin(login));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch customer by login: " + e.getMessage());
        }
    }

    public Optional<CustomerLoginDto> verifyCustomerLogin(String login, String rawPassword){
        try {
            logger.debug("Start to log customer with login {} ", login);

            Optional<CustomerLogin> customerLoginFound = Optional.of(customerLoginRepository.findByLogin(login));

            if(customerLoginFound.isPresent()){
                if(passwordEncoder.matches(rawPassword, customerLoginFound.get().getPassword())){
                    Optional<CustomerLoginDto> customerLoginDtoFound = Optional.of(
                            customerLoginMapperImpl.convertToDto(customerLoginFound.get()));
                    
                    logger.debug("Customer {} logged successfully ", login);
                    
                    return customerLoginDtoFound;
                }
            }

            logger.debug("Customer {} not logged ", login);

            return Optional.empty();

        } catch (Exception e) {
            logger.error("Failed to log customer with login {} :", login);
            throw new RuntimeException("Failed to verify customer Login: " + e.getMessage());
        }
    }
}
