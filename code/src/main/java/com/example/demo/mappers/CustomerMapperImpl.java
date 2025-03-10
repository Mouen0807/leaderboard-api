package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.Customer;
import com.example.demo.models.Role;

import java.util.List;
import java.util.UUID;

public class CustomerMapperImpl implements CustomerMapper {
    private final CustomerDetailsMapper customerDetailsMapper = new CustomerDetailsMapperImpl();

    @Override
    public CustomerDto convertToDto(Customer customer) {
        if(customer == null) return null;

        CustomerDto customerDto = CustomerDto.builder()
                .id(customer.getId().toString())
                .login(customer.getLogin())
                .password(customer.getPassword())
                .role(customer.getRole().getName())
                .customerDetails(customerDetailsMapper.convertToDto(customer.getCustomerDetails()))
                .createdAt(customer.getCreatedAt().toString())
                .updatedAt(customer.getUpdatedAt().toString())
                .build();

        return customerDto;
    }

    @Override
    public List<CustomerDto> convertToDto(List<Customer> customer) {
        return null;
    }

    public Customer convertToEntityWithRole(CustomerDto customerDto, Role role) {
        if(customerDto == null) return null;

        Customer customer = Customer.builder()
                .login(customerDto.getLogin())
                .password(customerDto.getPassword())
                .customerDetails(customerDetailsMapper.convertToEntity(customerDto.getCustomerDetails()))
                .role(role)
                .build();

        if(customerDto.getId() != null) customer.setId(UUID.fromString(customerDto.getId()));

        return customer;
    }

    public Customer convertToEntityWithoutRole(CustomerDto customerDto) {
        if(customerDto == null) return null;

        Customer customer = Customer.builder()
                .login(customerDto.getLogin())
                .password(customerDto.getPassword())
                .customerDetails(customerDetailsMapper.convertToEntity(customerDto.getCustomerDetails()))
                .build();

        if(customerDto.getId() != null) customer.setId(UUID.fromString(customerDto.getId()));

        return customer;
    }
}
