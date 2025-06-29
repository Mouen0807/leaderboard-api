package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.mappers.JwtTokenMapper;
import com.example.demo.models.Customer;
import com.example.demo.models.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtTokenMapper jwtTokenMapper;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", "test-secret-key");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 100_000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 200_000L);
    }

    @Test
    public void shouldConstructAccessAndRefreshTokens() {
        // Given
        CustomerDto dto = CustomerDto.builder()
                .id("user123")
                .login("john.doe")
                .build();

        List<String> permissions = List.of("READ", "WRITE");
        String role = "ADMIN";

        JwtTokenDto jwtTokenDtoDto = new JwtTokenDto();
        when(jwtTokenMapper.convertToDto(any(JwtToken.class))).thenReturn(jwtTokenDtoDto);

        // When
        JwtTokenDto result = jwtService.constructToken(dto, role, permissions);

        // Then
        assertNotNull(result);
        //verify(jwtTokenMapper, times(1)).convertToDto(any(JwtToken.class));
    }

    @Test
    public void shouldExtractPermissions() {
        // Given
        String token = JWT.create()
                .withClaim("permissions", "[READ,WRITE]")
                .sign(Algorithm.HMAC256("test-secret-key"));

        // When
        List<String> permissions = jwtService.extractPermissions(token);

        // Then
        assertEquals(List.of("READ", "WRITE"), permissions);
    }

    @Test
    public void shouldValidateTokenAgainstCustomer() {
        // Given
        UUID customerId = UUID.randomUUID();

        CustomerDto dto = CustomerDto.builder()
                .id(customerId.toString())
                .login("john.doe")
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .build();

        String token = jwtService.generateAccessToken(Map.of(), dto);

        // When
        boolean valid = jwtService.isTokenValid(token, customer);

        // Then
        assertTrue(valid);
    }

    @Test
    public void shouldValidateTokenAgainstUserDetails() {
        // Given
        String customerId = "user-456";
        CustomerDto dto = CustomerDto.builder()
                .id(customerId)
                .login("john.doe")
                .build();

        when(userDetails.getUsername()).thenReturn(customerId);

        String token = jwtService.generateAccessToken(Map.of(), dto);

        // When
        boolean valid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertTrue(valid);
    }
}
