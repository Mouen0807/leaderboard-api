package com.example.demo.controllers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.dtos.RoleDto;
import com.example.demo.models.LoginInput;
import com.example.demo.models.Role;
import com.example.demo.services.JwtService;
import com.example.demo.services.RedisCacheService;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.CustomerService;
import com.example.demo.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/auth/create")
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDto customerDto) {
        logger.info("Attempt to create customer with login: {} ", customerDto.getLogin());

        Optional<CustomerDto> customerLoginOpt = customerService.findCustomer(customerDto.getLogin());
        
        if(customerLoginOpt.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.CONFLICT.toString())
                                        .message("Customer Login already exists")
                                        .build();
            logger.info("customer not created because login already exists");

            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.CONFLICT);
        }

        Optional<RoleDto> roleOpt = roleService.findRoleByName(customerDto.getRole());

        if(roleOpt.isEmpty()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role doesn't exist")
                    .build();
            logger.info("customer not created because role doesn't exist");

            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        Optional<CustomerDto> optCustomerSaved = customerService.createCustomer(customerDto);
        JwtTokenDto jwtTokenDto= jwtService.constructToken(optCustomerSaved.get(),
                roleOpt.get().getName(),
                roleOpt.get().getPermissions());
        
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Customer created")
                .data(jwtTokenDto)
                .build();
        logger.info("Customer created");

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginInput loginInput) {
        logger.info("Attempt to authenticate customer with login: {} ", loginInput.getLogin());

        Optional<CustomerDto> optCustomerLoginDto = customerService.verifyCustomer(
                loginInput.getLogin(),
                loginInput.getPassword());

        if(optCustomerLoginDto.isEmpty()){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.UNAUTHORIZED.toString())
                                        .message("Customer is not authenticated")
                                        .build();

            logger.info("Customer is not authenticated successfully");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.UNAUTHORIZED);
        }

        Optional<RoleDto> roleOpt = roleService.findRoleByName(optCustomerLoginDto.get().getRole());
        JwtTokenDto jwtTokenDto= jwtService.constructToken(optCustomerLoginDto.get(),
                roleOpt.get().getName(),
                roleOpt.get().getPermissions());

        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Customer is authenticated")
                .data(jwtTokenDto)
                .build();

        logger.info("Customer is correctly authenticated");
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logoutCustomer(@RequestBody JwtTokenDto jwtTokenDto) {
        logger.info("Attempt to logout customer");

        if(jwtService.isTokenExpired(jwtTokenDto.getAccessToken())){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Access token is expired")
                                        .build();

            logger.info("Access token is expired");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        if(jwtService.isTokenExpired(jwtTokenDto.getRefreshToken())){
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Refresh token is expired")
                                        .build();

            logger.info("Refresh token is expired");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        String accessTokenLogin = jwtService.extractLogin(jwtTokenDto.getAccessToken());
        String refreshTokenLogin = jwtService.extractLogin(jwtTokenDto.getRefreshToken());

        if(!accessTokenLogin.equals(refreshTokenLogin)){
            ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Access and refresh token subject are not equals")
                                        .build();

            logger.info("Access and refresh token subject are not equals");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        String accessTokenId = jwtService.extractId(jwtTokenDto.getAccessToken());
        String refreshTokenId = jwtService.extractId(jwtTokenDto.getRefreshToken());

        redisCacheService.revokeToken(accessTokenId,
            "ACCESS-TOKEN",
            jwtService.extractExpiration(jwtTokenDto.getAccessToken()).getTime());

        redisCacheService.revokeToken(refreshTokenId,
            "REFRESH-TOKEN",
            jwtService.extractExpiration(jwtTokenDto.getAccessToken()).getTime());

        ApiResponse apiResponse = ApiResponse.builder()
                                        .code(HttpStatus.OK.toString())
                                        .message("Tokens are revoked")
                                        .build();

        logger.info("Customer with login {} is correctly logout",accessTokenLogin);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
  
    }

}
