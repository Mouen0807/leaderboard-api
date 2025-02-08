package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.Customer;
import com.example.demo.models.Role;

import java.util.List;

public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = CustomerDto.builder()
                .id(customer.getId().toString())
                .login(customer.getLogin())
                .role(customer.getRole().getName())
                .customerDetails(customer.getCustomerDetails())
                .createdAt(customer.getCreatedAt().toString())
                .updatedAt(customer.getUpdatedAt().toString())
                .build();

        return customerDto;
    }

    @Override
    public List<CustomerDto> convertToDto(List<Customer> customer) {
        return null;
    }

    public Customer convertToEntity(CustomerDto customerDto, Role role) {
        Customer customer = Customer.builder()
                .login(customerDto.getLogin())
                .password(customerDto.getPassword())
                .customerDetails(customerDto.getCustomerDetails())
                .role(role)
                .build();

        return customer;
    }
}
