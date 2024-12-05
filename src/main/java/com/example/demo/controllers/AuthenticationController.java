package com.example.demo.controllers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.LoginInput;
import com.example.demo.services.JwtService;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.models.CustomerLogin;
import com.example.demo.services.CustomerLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final CustomerLoginService customerLoginService;
    private JwtService jwtService;

    public AuthenticationController(CustomerLoginService customerLoginService, JwtService jwtService) {
        this.customerLoginService = customerLoginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/create")
    public ResponseEntity<JwtTokenDto> saveCustomer(@RequestBody CustomerLogin customerLogin) {
        CustomerLoginDto savedCustomerLogin = customerLoginService.createCustomerLogin(customerLogin);
        JwtTokenDto jwtTokenDto = jwtService.constructToken(savedCustomerLogin);

        return ResponseEntity.ok(jwtTokenDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtTokenDto> loginCustomer(@RequestBody LoginInput loginInput) {
        Optional<CustomerLoginDto> customerLogin = customerLoginService.verifyCustomerLogin(
                loginInput.getLogin(),
                loginInput.getPassword());

        if(customerLogin.isPresent()){
            JwtTokenDto jwtTokenDto= jwtService.constructToken(customerLogin.get());
            return ResponseEntity.ok(jwtTokenDto);
        }

        return ResponseEntity.notFound().build();
    }

}
