package com.example.demo.services;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.mappers.CustomerDetailsMapper;
import com.example.demo.mappers.CustomerDetailsMapperImpl;
import com.example.demo.mappers.CustomerMapperImpl;

import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.models.Customer;

import com.example.demo.repositories.RoleRepository;
import com.example.demo.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerMapperImpl customerMapperImpl = new CustomerMapperImpl();
    private final CustomerDetailsMapper customerDetailsMapper = new CustomerDetailsMapperImpl();

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<CustomerDto> findCustomerByLogin(String login){
        try {
            logger.debug("Start find customer with login {}", login);

            Optional<Customer> optCustomer = Optional.ofNullable(customerRepository.findByLogin(login));

            if (optCustomer.isPresent()) {
                logger.debug("Customer found");
                return Optional.of(customerMapperImpl.convertToDto(optCustomer.get()));
            }

            logger.debug("Customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to find customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> findCustomerByGuid(String customerGuid){
        try {
            logger.debug("Start find customer with id {}", customerGuid);

            if(!Validator.isValidUUID(customerGuid)){
                logger.debug("Customer not found");
                return Optional.empty();
            }

            Optional<Customer> optCustomer = customerRepository.findById(UUID.fromString(customerGuid));

            if (optCustomer.isPresent()) {
                logger.debug("Customer found");
                return Optional.of(customerMapperImpl.convertToDto(optCustomer.get()));
            }

            logger.debug("Customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to find customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> updateLogin(String customerGuid, CustomerLoginDto customerLoginDto){
        try {
            logger.debug("Start to update customer {} using login {}", customerGuid, customerLoginDto);

            Optional<Customer> optCustomer = customerRepository.findById(UUID.fromString(customerGuid));

            if (optCustomer.isPresent()) {
                Customer customer = optCustomer.get();
                
                customer.setLogin(customerLoginDto.getLogin());
                String customerPasswordEncoded = customer.getPassword();
                customer.setPassword(passwordEncoder.encode(customerPasswordEncoded));
                
                Customer customerSaved = customerRepository.save(customer);

                logger.debug("Customer updated");
                return Optional.of(customerMapperImpl.convertToDto(customerSaved));
            }

            logger.debug("Customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to update customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> updateRole(String customerGuid, Role role){
        try {
            logger.debug("Start to update customer {} using role {}", customerGuid, role);

            Optional<Customer> optCustomer = customerRepository.findById(UUID.fromString(customerGuid));

            if (optCustomer.isPresent()) {
                Customer customer = optCustomer.get();
                customer.setRole(role);
                Customer customerSaved = customerRepository.save(customer);

                logger.debug("Customer updated");
                return Optional.of(customerMapperImpl.convertToDto(customerSaved));
            }

            logger.debug("Customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to update customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> updateCustomerDetails(String customerGuid, CustomerDetailsDto customerDetailsDto){
        try {
            logger.debug("Start to update customer {} using customerDetails {}", customerGuid, customerDetailsDto);

            Optional<Customer> optCustomer = customerRepository.findById(UUID.fromString(customerGuid));

            if (optCustomer.isPresent()) {
                Customer customer = optCustomer.get();
                customer.setCustomerDetails(customerDetailsMapper.convertToEntity(customerDetailsDto));
                Customer customerSaved = customerRepository.save(customer);

                logger.debug("Customer updated");
                return Optional.of(customerMapperImpl.convertToDto(customerSaved));
            }

            logger.debug("Customer not found");
            return Optional.empty();
        }catch (Exception e) {
            logger.error("Failed to update customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> saveCustomerWithRole(CustomerDto customerDto, Role role){
        try {
            logger.debug("Start saving customer {}", customerDto);

            String customerPassword = customerDto.getPassword();
            customerDto.setPassword(passwordEncoder.encode(customerPassword));

            Customer customerToSave = customerMapperImpl.convertToEntityWithRole(customerDto, role);
            Customer customerSaved = customerRepository.save(customerToSave);

            logger.debug("Customer is saved");
            return Optional.of(customerMapperImpl.convertToDto(customerSaved));
        } catch (Exception e) {
            logger.error("Failed to save customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Customer> saveCustomer(CustomerDto customerDto){
        try {
            logger.debug("Start saving customer {}", customerDto);

            Optional<Customer> optCheckLogin = Optional.ofNullable(customerRepository.findByLogin(customerDto.getLogin()));
            if(optCheckLogin.isPresent()){
                logger.debug("Customer login already exists");
                return Optional.empty();
            }

            Optional<Role> optCheckRole = Optional.ofNullable(roleRepository.findByName(customerDto.getRole()));
            if(optCheckRole.isEmpty()){
                logger.debug("Customer role is not found");
                return Optional.empty();
            }

            String customerPassword = customerDto.getPassword();
            customerDto.setPassword(passwordEncoder.encode(customerPassword));

            Customer customerToSave = customerMapperImpl.convertToEntityWithRole(customerDto, optCheckRole.get());
            Customer customerSaved = customerRepository.save(customerToSave);

            logger.debug("Customer is saved");
            return Optional.of(customerSaved);
        } catch (Exception e) {
            logger.error("Failed to save customer");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CustomerDto> loginCustomer(String login, String rawPassword){
        try {
            logger.debug("Start to login customer using lgin {}", login);

            Optional<Customer> customerLoginFound = Optional.ofNullable(customerRepository.findByLogin(login));

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
