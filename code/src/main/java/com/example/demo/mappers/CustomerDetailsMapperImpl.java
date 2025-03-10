package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.models.CustomerDetails;

import java.util.List;
import java.util.UUID;

public class CustomerDetailsMapperImpl implements CustomerDetailsMapper{
    @Override
    public CustomerDetailsDto convertToDto(CustomerDetails customerDetails) {
        if(customerDetails == null) return null;

        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetails.getId().toString())
                .firstName(customerDetails.getFirstName())
                .secondName(customerDetails.getSecondName())
                .dateOfBirth(customerDetails.getDateOfBirth())
                .phoneNumber(customerDetails.getPhoneNumber())
                .email(customerDetails.getEmail())
                .originCountry(customerDetails.getOriginCountry())
                .build();

        return customerDetailsDto;
    }

    @Override
    public List<CustomerDetailsDto> convertToDto(List<CustomerDetails> customerDetailsList) {
        return List.of();
    }

    @Override
    public CustomerDetails convertToEntity(CustomerDetailsDto customerDetailsDto) {
        if(customerDetailsDto == null) return null;

        CustomerDetails customerDetails = CustomerDetails.builder()
                .firstName(customerDetailsDto.getFirstName())
                .secondName(customerDetailsDto.getSecondName())
                .dateOfBirth(customerDetailsDto.getDateOfBirth())
                .phoneNumber(customerDetailsDto.getPhoneNumber())
                .email(customerDetailsDto.getEmail())
                .originCountry(customerDetailsDto.getOriginCountry())
                .build();

        if(customerDetailsDto.getId() != null) customerDetails.setId(UUID.fromString(customerDetailsDto.getId()));

        return customerDetails;
    }
}
