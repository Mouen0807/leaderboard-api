package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.models.CustomerDetails;

import java.util.List;

public interface CustomerDetailsMapper {
    public CustomerDetailsDto convertToDto(CustomerDetails customerDetails);
    public List<CustomerDetailsDto> convertToDto(List<CustomerDetails> customerDetailsList);
    public CustomerDetails convertToEntity(CustomerDetailsDto customerDetailsDto);
}
