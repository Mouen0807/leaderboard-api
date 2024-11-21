package com.example.demo.services;

import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.mappers.JwtTokenMapper;
import com.example.demo.mappers.JwtTokenMapperImpl;
import com.example.demo.models.CustomerLogin;
import com.example.demo.models.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final JwtTokenMapperImpl jwtTokenMapperImpl = new JwtTokenMapperImpl();

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public JwtTokenDto constructToken(CustomerLoginDto customerLoginDto){
        JwtToken jwtToken = new JwtToken();
        Map<String, Object> extraClaims = new HashMap<String,Object>();

        extraClaims.put("role",customerLoginDto.getRole());
        extraClaims.put("permissions",customerLoginDto.getPermissions());

        jwtToken.setAccessToken(generateToken(extraClaims,customerLoginDto));
        jwtToken.setRefreshToken(generateRefreshToken(customerLoginDto));

        return jwtTokenMapperImpl.convertToDto(jwtToken);
    }

    public String generateToken(CustomerLoginDto customerLoginDto) {
        return generateToken(new HashMap<>(), customerLoginDto);
    }

    public String generateToken(
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
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(customerLoginDto.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, CustomerLogin customerLogin) {
        final String login = extractLogin(token);
        return (login.equals(customerLogin.getLogin())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String customerLogin = extractLogin(token);
        return (customerLogin.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        System.out.println("TEST " +  refreshExpiration);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
