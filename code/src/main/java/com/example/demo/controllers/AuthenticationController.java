package com.example.demo.controllers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.dtos.RoleDto;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.models.Customer;
import com.example.demo.models.LoginInput;
import com.example.demo.services.JwtService;
import com.example.demo.services.RedisCacheService;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.CustomerService;
import com.example.demo.services.RoleService;
import jakarta.validation.Valid;
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
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/auth/create")
    public ResponseEntity<?> saveCustomer(@Valid  @RequestBody CustomerDto customerDto) {
        logger.info("Attempt to create customer with login: {} ", customerDto);

        Optional<Customer> optCustomerSaved = customerService.saveCustomer(customerDto);
        if(optCustomerSaved.isEmpty()){
            ApiResponse<CustomerDto> apiResponse = ApiResponse.<CustomerDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Login already exists, or role not found")
                    .build();

            logger.info("Login already exists, or role not found");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        CustomerDto customerSavedDto = customerMapper.convertToDto(optCustomerSaved.get());

        JwtTokenDto jwtTokenDto = jwtService.constructToken(customerSavedDto,
                optCustomerSaved.get().getRole().getName(),
                optCustomerSaved.get().getRole().getPermissionNames());
        
        ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Customer created")
                .data(jwtTokenDto)
                .build();

        logger.info("Customer created");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginInput loginInput) {
        logger.info("Attempt to authenticate customer with login {} ", loginInput.getLogin());

        Optional<CustomerDto> optCustomerLoginDto = customerService.loginCustomer(
                loginInput.getLogin(),
                loginInput.getPassword());

        if(optCustomerLoginDto.isEmpty()){
            ApiResponse<LoginInput> apiResponse = ApiResponse.<LoginInput> builder()
                                        .code(HttpStatus.UNAUTHORIZED.toString())
                                        .message("Customer not authenticated")
                                        .build();

            logger.info("Customer not authenticated");
            return new ResponseEntity<>(apiResponse,HttpStatus.UNAUTHORIZED);
        }

        Optional<RoleDto> roleOpt = roleService.findRoleByName(optCustomerLoginDto.get().getRole());
        JwtTokenDto jwtTokenDto= jwtService.constructToken(optCustomerLoginDto.get(),
                roleOpt.get().getName(),
                roleOpt.get().getPermissions());

        ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Customer is authenticated")
                .data(jwtTokenDto)
                .build();

        logger.info("Customer is authenticated");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logoutCustomer(@Valid @RequestBody JwtTokenDto jwtTokenDto) {
        logger.info("Attempt to logout a customer");

        if(jwtService.isTokenExpired(jwtTokenDto.getAccessToken())){
            ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto> builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Access token is expired")
                                        .build();

            logger.info("Access token is expired");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        if(jwtService.isTokenExpired(jwtTokenDto.getRefreshToken())){
            ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto> builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Refresh token is expired")
                                        .build();

            logger.info("Refresh token is expired");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        String accessTokenSubject = jwtService.extractSubject(jwtTokenDto.getAccessToken());
        String refreshTokenSubject = jwtService.extractSubject(jwtTokenDto.getRefreshToken());

        if(!accessTokenSubject.equals(refreshTokenSubject)){
            ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto> builder()
                                        .code(HttpStatus.BAD_REQUEST.toString())
                                        .message("Access and refresh token subject are not equals")
                                        .build();

            logger.info("Access and refresh token subject are not equals");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        String accessTokenId = jwtService.extractId(jwtTokenDto.getAccessToken());
        String refreshTokenId = jwtService.extractId(jwtTokenDto.getRefreshToken());

        redisCacheService.revokeToken(accessTokenId,
            "ACCESS-TOKEN",
            jwtService.extractExpiration(jwtTokenDto.getAccessToken()).getTime());

        redisCacheService.revokeToken(refreshTokenId,
            "REFRESH-TOKEN",
            jwtService.extractExpiration(jwtTokenDto.getAccessToken()).getTime());

        ApiResponse<JwtTokenDto> apiResponse = ApiResponse.<JwtTokenDto> builder()
                                        .code(HttpStatus.OK.toString())
                                        .message("Tokens are revoked")
                                        .build();

        logger.info("Customer {} is correctly logout", accessTokenSubject);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
  
    }

}
