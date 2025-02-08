package com.example.demo.services;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.mappers.CustomerMapperImpl;

import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.models.Customer;

import com.example.demo.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerMapperImpl customerMapperImpl = new CustomerMapperImpl();

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerLoginRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<CustomerDto> findCustomer(String login){
        try {
            logger.debug("start find customer with login: {} ", login);

            Optional<Customer> optCustomer = Optional.ofNullable(customerLoginRepository.findByLogin(login));

            if (optCustomer.isPresent()) {
                logger.debug("customer found");
                return Optional.of(customerMapperImpl.convertToDto(optCustomer.get()));
            }

            logger.debug("customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to find customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> createCustomer(CustomerDto customerDto){
        try {
            logger.debug("Start saving customer with login: {} ", customerDto.getLogin());

            String customerPassword = customerDto.getPassword();
            customerDto.setPassword(passwordEncoder.encode(customerPassword));

            Role role = roleRepository.findByName(customerDto.getRole());
            Customer customerToSaved = customerMapperImpl.convertToEntity(customerDto,role);

            Customer customerSaved = customerLoginRepository.save(customerToSaved);

            logger.debug("Customer is saved");
            return Optional.of(customerMapperImpl.convertToDto(customerSaved));
        } catch (Exception e) {
            logger.error("Failed to save customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> verifyCustomer(String login, String rawPassword){
        try {
            logger.debug("Start to log customer with login {} ", login);

            Optional<Customer> customerLoginFound = Optional.ofNullable(customerLoginRepository.findByLogin(login));

            if(customerLoginFound.isPresent()){
                if(passwordEncoder.matches(rawPassword, customerLoginFound.get().getPassword())){
                    Optional<CustomerDto> optCustomerDtoFound = Optional.of(
                            customerMapperImpl.convertToDto(customerLoginFound.get()));

                    logger.debug("Customer is logged successfully");
                    return optCustomerDtoFound;
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
