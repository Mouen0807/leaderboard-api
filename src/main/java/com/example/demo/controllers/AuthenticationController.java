package com.example.demo.controllers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.LoginInput;
import com.example.demo.services.JwtService;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.models.CustomerLogin;
import com.example.demo.services.CustomerLoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final CustomerLoginService customerLoginService;
    private final JwtService jwtService;

    public AuthenticationController(CustomerLoginService customerLoginService, JwtService jwtService) {
        this.customerLoginService = customerLoginService;
        this.jwtService = jwtService;
    }  

    @PostMapping("/auth/create")
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerLogin customerLogin) {
        logger.info("Attempt to create customer with login: {} ", customerLogin.getLogin());
        
        Optional<CustomerLoginDto> savedCustomerLoginDto = customerLoginService.createCustomerLogin(customerLogin);
        
        if(!savedCustomerLoginDto.isPresent()){
            logger.warn("Customer not created because login {} alread exists", customerLogin.getLogin());
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.CONFLICT.toString())
                                        .message("Customer Login already exist")
                                        .build();

            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.CONFLICT);
        }
        
        JwtTokenDto jwtTokenDto= jwtService.constructToken(savedCustomerLoginDto.get());
        
        logger.info("Customer created with login : {} ", customerLogin.getLogin());

        return new ResponseEntity<JwtTokenDto>(jwtTokenDto, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginInput loginInput) {
        logger.info("Attempt to authenticate customer : {} ", loginInput.getLogin());

        Optional<CustomerLoginDto> customerLogin = customerLoginService.verifyCustomerLogin(
                loginInput.getLogin(),
                loginInput.getPassword());

        if(!customerLogin.isPresent()){
            logger.warn("Customer {} is not authenticated successfully", loginInput.getLogin());

            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.UNAUTHORIZED.toString())
                                        .message("Customer not authenticated")
                                        .build();
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.UNAUTHORIZED);
        }

        JwtTokenDto jwtTokenDto= jwtService.constructToken(customerLogin.get());

        logger.info("Customer {} is correctly authenticated ", loginInput.getLogin());

        return ResponseEntity.ok(jwtTokenDto);
  
    }

}
