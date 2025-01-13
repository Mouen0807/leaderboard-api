package com.example.demo.mappers;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.models.CustomerLogin;

import java.util.List;

public interface CustomerLoginMapper {
    public CustomerLoginDto convertToDto(CustomerLogin customerLogin);
    public List<CustomerLoginDto> convertToDto(List<CustomerLogin> customerLogin);

}
