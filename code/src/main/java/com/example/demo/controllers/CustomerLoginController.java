package com.example.demo.controllers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.CustomerService;

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
    private final CustomerService customerService;

    public CustomerLoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer/infos")
    public ResponseEntity<?> customerInfos(@RequestParam String login) {
        logger.info("Attempt to get customer infos for login: {} ", login);
        Optional<CustomerDto> optCustomer  = customerService.findCustomer(login);

        if(optCustomer.isEmpty()){
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
