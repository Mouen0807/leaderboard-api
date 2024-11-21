package com.example.demo.controllers;

import com.example.demo.services.JwtService;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.services.CustomerLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
public class CustomerLoginController {
    private final CustomerLoginService customerLoginService;
    private final JwtService jwtService;

    public CustomerLoginController(CustomerLoginService customerLoginService, JwtService jwtService) {
        this.customerLoginService = customerLoginService;
        this.jwtService = jwtService;
    }

    @GetMapping("/customer/details")
    public ResponseEntity<CustomerLoginDto> customerInfos(@RequestParam String login) {
        CustomerLoginDto customerLoginDto = customerLoginService.fetchCustomerLoginByLogin(login);
        return ResponseEntity.ok(customerLoginDto);
    }
}
