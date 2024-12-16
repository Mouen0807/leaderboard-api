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
    private final CustomerLoginService customerLoginService;
    //private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginController.class);

    public CustomerLoginController(CustomerLoginService customerLoginService) {
        this.customerLoginService = customerLoginService;
    }

    @GetMapping("/customer/details")
    public ResponseEntity<?> customerInfos(@RequestParam String login) {
        logger.info("Attempt to get customer details for login: {} ", login);

        Optional<CustomerLoginDto> optCustomer  = customerLoginService.fetchCustomerLoginByLogin(login);

        if(!optCustomer.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.NOT_FOUND.toString())
                                        .message("Customer details not found")
                                        .build();

            logger.info("customer details not found for login: {} ", login);

            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.CONFLICT);
        }

        logger.info("customer details successfully found for login: {} ", login);

        return ResponseEntity.ok(optCustomer.get());
    }
}
