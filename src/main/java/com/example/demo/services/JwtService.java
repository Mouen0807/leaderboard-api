package com.example.demo.services;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.mappers.JwtTokenMapperImpl;
import com.example.demo.models.CustomerLogin;
import com.example.demo.models.JwtToken;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final JwtTokenMapperImpl jwtTokenMapperImpl = new JwtTokenMapperImpl();
    
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public JwtTokenDto constructToken(CustomerLoginDto customerLoginDto){
        logger.debug("Start token creation for login {} ", customerLoginDto.getLogin());

        JwtToken jwtToken = new JwtToken();
        Map<String, Object> extraClaims = new HashMap<String,Object>();

        extraClaims.put("role",customerLoginDto.getRole());
        extraClaims.put("permissions",customerLoginDto.getPermissions());

        jwtToken.setAccessToken(generateAccessToken(extraClaims,customerLoginDto));
        jwtToken.setRefreshToken(generateRefreshToken(customerLoginDto));

        logger.debug("Token created for login {} ", customerLoginDto.getLogin());

        return jwtTokenMapperImpl.convertToDto(jwtToken);
    }

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomerLoginDto customerLoginDto
    ) {
        return buildToken(extraClaims, customerLoginDto, jwtExpiration);
    }

    public String generateRefreshToken(
            CustomerLoginDto customerLoginDto
    ) {
        return buildToken(new HashMap<>(), customerLoginDto, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            CustomerLoginDto customerLoginDto,
            long expiration
    ) {
        long currentDate = System.currentTimeMillis();

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        // Build the token
        var tokenBuilder = JWT.create()
            .withSubject(customerLoginDto.getLogin())
            .withIssuedAt(new Date(currentDate))  
            .withExpiresAt(new Date(currentDate + expiration))
            .withJWTId(customerLoginDto.getLogin()+":"+UUID.randomUUID()+":"+currentDate);

        for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
            tokenBuilder.withClaim(entry.getKey(), entry.getValue().toString()); 
        }
        
        return tokenBuilder.sign(algorithm); 
    }

    public boolean isTokenValid(String token, CustomerLogin customerLogin) {
        final String login = extractLogin(token);
        return (login.equals(customerLogin.getLogin())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String customerLogin = extractLogin(token);
        return (customerLogin.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractLogin(String token) {
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
