package com.example.demo.mappers;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.models.CustomerLogin;
import com.example.demo.models.Permission;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerLoginMapperImpl implements CustomerLoginMapper {


    @Override
    public CustomerLoginDto convertToDto(CustomerLogin customerLogin) {

        List<String> permissionNames = customerLogin.getRole()
                .getPermissions()
                .stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .collect(Collectors.toList());

        CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
                .id(customerLogin.getId().toString())
                .login(customerLogin.getLogin())
                .role(customerLogin.getRole().getName())
                .customerDetails(customerLogin.getCustomerDetails())
                .permissions(permissionNames)
                .createdAt(customerLogin.getCreatedAt().toString())
                .updatedAt(customerLogin.getUpdatedAt().toString())
                .build();

        return customerLoginDto;
    }

    @Override
    public List<CustomerLoginDto> convertToDto(List<CustomerLogin> customerLogin) {
        return null;
    }
}
