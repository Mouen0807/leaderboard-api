package com.example.demo.services;

import com.example.demo.models.JwtToken;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.mappers.CustomerLoginMapperImpl;
import com.example.demo.repositories.CustomerLoginRepository;
import com.example.demo.models.CustomerLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerLoginService {

    private final CustomerLoginRepository customerLoginRepository;
    private final CustomerLoginMapperImpl customerLoginMapperImpl = new CustomerLoginMapperImpl();
    @Autowired
    private PasswordEncoder passwordEncoder;


    public CustomerLoginService(CustomerLoginRepository customerLoginRepository) {
        this.customerLoginRepository = customerLoginRepository;
    }

    public CustomerLoginDto createCustomerLogin(CustomerLogin customerLogin){
        try {
            String customerPassword = customerLogin.getPassword();
            customerLogin.setPassword(passwordEncoder.encode(customerPassword));

            return customerLoginMapperImpl.convertToDto(customerLoginRepository.save(customerLogin));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }

    public CustomerLoginDto fetchCustomerLoginByLogin(String login) {
        try {
            return customerLoginMapperImpl.convertToDto(customerLoginRepository.findById(login).get());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch customer by login: " + e.getMessage());
        }
    }

    public Optional<CustomerLogin> fetchCustomerByLogin(String login) {
        try {
            return customerLoginRepository.findById(login);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch customer by login: " + e.getMessage());
        }
    }

    public Optional<CustomerLoginDto> verifyCustomerLogin(String login, String rawPassword){
        try {
            Optional<CustomerLogin> customerLoginFound = customerLoginRepository.findById(login);

            if(customerLoginFound.isPresent()){
                if(passwordEncoder.matches(rawPassword, customerLoginFound.get().getPassword())){
                    Optional<CustomerLoginDto> customerLoginDtoFound = Optional.of(
                            customerLoginMapperImpl.convertToDto(customerLoginFound.get()));

                    return customerLoginDtoFound;
                }
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify customer Login: " + e.getMessage());
        }
    }
}
