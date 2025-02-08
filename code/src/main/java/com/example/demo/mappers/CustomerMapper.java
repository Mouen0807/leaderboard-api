package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.Customer;

import java.util.List;

public interface CustomerMapper {
    public CustomerDto convertToDto(Customer customer);
    public List<CustomerDto> convertToDto(List<Customer> customer);
}
