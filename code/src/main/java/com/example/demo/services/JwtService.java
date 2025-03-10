package com.example.demo.services;

import com.auth0.jwt.interfaces.Claim;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.mappers.JwtTokenMapperImpl;
import com.example.demo.models.Customer;
import com.example.demo.models.JwtToken;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final JwtTokenMapperImpl jwtTokenMapperImpl = new JwtTokenMapperImpl();

    public List<String> extractPermissions(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        String permissionsClaim = decodedJWT.getClaim("permissions").asString();
        List<String> permissions = Arrays.asList(permissionsClaim.replaceAll("[\\[\\]\\s]", "").split(","));

        return permissions;
    }

    public JwtTokenDto constructToken(CustomerDto customerDto, String role, List<String> permissions){
        logger.debug("Attempt to create tokens for login {} ", customerDto.getLogin());

        JwtToken jwtToken = new JwtToken();
        Map<String, Object> extraClaims = new HashMap<String,Object>();

        extraClaims.put("role",role);
        extraClaims.put("permissions", permissions);

        jwtToken.setAccessToken(generateAccessToken(extraClaims, customerDto));
        logger.debug("Access Token is created");

        jwtToken.setRefreshToken(generateRefreshToken(customerDto));
        logger.debug("Refresh Token is created");

        return jwtTokenMapperImpl.convertToDto(jwtToken);
    }

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomerDto customerDto
    ) {
        return buildToken(extraClaims, customerDto, jwtExpiration);
    }

    public String generateRefreshToken(
            CustomerDto customerDto
    ) {
        return buildToken(new HashMap<>(), customerDto, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            CustomerDto customerDto,
            long expiration
    ) {
        long currentDate = System.currentTimeMillis();

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        // Build the token
        var tokenBuilder = JWT.create()
            .withSubject(customerDto.getId())
            .withIssuedAt(new Date(currentDate))  
            .withExpiresAt(new Date(currentDate + expiration))
            .withJWTId(customerDto.getLogin()+":"+UUID.randomUUID()+":"+currentDate);

        for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
            tokenBuilder.withClaim(entry.getKey(), entry.getValue().toString());
        }
        
        return tokenBuilder.sign(algorithm); 
    }

    public boolean isTokenValid(String token, Customer customer) {
        final String customerId = extractSubject(token);
        return (customerId.equals(customer.getId().toString())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String customerId = extractSubject(token);
        return (customerId.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractSubject(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public String extractId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getId();
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }

    public Date extractExpiration(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

}
