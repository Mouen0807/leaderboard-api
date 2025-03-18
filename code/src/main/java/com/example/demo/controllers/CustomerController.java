package com.example.demo.controllers;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.models.Role;
import com.example.demo.services.CustomerService;

import java.util.Optional;

import com.example.demo.services.JwtService;
import com.example.demo.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.ValidationAnnotationUtils.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/customer/infos")
    public ResponseEntity<?> getcustomerInfos(@RequestParam String login) {
        logger.info("Attempt to get customer infos for login: {} ", login);
        Optional<CustomerDto> optCustomerDto  = customerService.findCustomerByLogin(login);

        if(optCustomerDto.isEmpty()){
            ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                .code(HttpStatus.NOT_FOUND.toString())
                .message("Customer infos not found")
                .build();

            logger.info("customer infos not found");
            return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
        }

        ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                .code(HttpStatus.OK.toString())
                .message("customer infos successfully found")
                .data(optCustomerDto.get())
                .build();

        logger.info("customer infos successfully found");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/customer/updateLogin/{customerGuid}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerLoginDto customerLoginDto, @PathVariable String customerGuid) {
        logger.info("Attempt to update customer login with id: {} by values {}", customerGuid, customerLoginDto);

        Optional<CustomerDto> optCheckLogin = customerService.findCustomerByLogin(customerLoginDto.getLogin());

        if(optCheckLogin.isPresent()){
            ApiResponse<CustomerLoginDto> apiResponse = ApiResponse.<CustomerLoginDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Customer login already exists")
                    .build();
            logger.info("Customer not updated because login already exists");

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        Optional<CustomerDto> optCustomerSaved = customerService.updateLogin(customerGuid, customerLoginDto);

        if(optCustomerSaved.isEmpty()){
            ApiResponse<CustomerLoginDto> apiResponse = ApiResponse.<CustomerLoginDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Customer doesn't exist")
                    .build();
            logger.info("Customer not updated because he doesn't exist");

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Customer login updated")
                .data(optCustomerSaved.get())
                .build();
        logger.info("Customer login updated");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/customer/updateDetails/{customerGuid}")
    public ResponseEntity<?> updateDetails(@Valid @RequestBody CustomerDetailsDto customerDetailsDto, @PathVariable String customerGuid) {
        logger.info("Attempt to update customer details with id: {} by values {}", customerGuid, customerDetailsDto);

        Optional<CustomerDto> optCustomerSaved = customerService.updateCustomerDetails(customerGuid, customerDetailsDto);

        if(optCustomerSaved.isEmpty()){
            ApiResponse<CustomerLoginDto> apiResponse = ApiResponse.<CustomerLoginDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Customer doesn't exist")
                    .build();
            logger.info("Customer not updated because he doesn't exist");

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Customer details updated")
                .data(optCustomerSaved.get())
                .build();
        logger.info("Customer details updated");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/customer/updateRole/{customerGuid}")
    public ResponseEntity<?> updateRole(@NotNull @RequestParam String role, @PathVariable String customerGuid){
        logger.info("Attempt to update customer id: {} by role {}", customerGuid, role);

        Optional<Role> optcheckRole = roleService.findEntityRoleByName(role);

        if(optcheckRole.isEmpty()){
            ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role doesn't exist")
                    .build();
            logger.info("Customer not updated because role doesn't exist");

            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        Optional<CustomerDto> optCustomerSaved = customerService.updateRole(customerGuid, optcheckRole.get());

        if(optCustomerSaved.isEmpty()){
            ApiResponse<CustomerLoginDto> apiResponse = ApiResponse.<CustomerLoginDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Customer doesn't exist")
                    .build();
            logger.info("Customer not updated because he doesn't exist");

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        JwtTokenDto jwtTokenDto= jwtService.constructToken(optCustomerSaved.get(),
                optcheckRole.get().getName(),
                optcheckRole.get().getPermissionNames());

        ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Customer role updated")
                .data(jwtTokenDto)
                .build();
        logger.info("Customer role updated");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
