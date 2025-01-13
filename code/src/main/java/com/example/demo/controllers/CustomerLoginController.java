package com.example.demo.controllers;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.CustomerLoginService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CustomerLoginController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginController.class);
    private final CustomerLoginService customerLoginService;

    public CustomerLoginController(CustomerLoginService customerLoginService) {
        this.customerLoginService = customerLoginService;
    }

    @GetMapping("/customer/infos")
    public ResponseEntity<?> customerInfos(@RequestParam String login) {
        logger.info("Attempt to get customer infos for login: {} ", login);
        Optional<CustomerLoginDto> optCustomer  = customerLoginService.fetchCustomerLoginByLogin(login);

        if(!optCustomer.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.NOT_FOUND.toString())
                                        .message("Customer infos not found")
                                        .build();

            logger.info("customer infos not found");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.CONFLICT);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("customer infos successfully found")
                .data(optCustomer.get())
                .build();

        logger.info("customer infos successfully found");
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }
}
