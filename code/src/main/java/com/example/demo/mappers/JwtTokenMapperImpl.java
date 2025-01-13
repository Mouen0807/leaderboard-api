package com.example.demo.mappers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.JwtToken;

import java.util.List;

public class JwtTokenMapperImpl implements JwtTokenMapper {
    @Override
    public JwtTokenDto convertToDto(JwtToken jwtToken) {

        return JwtTokenDto.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

    public JwtTokenDto convertToDtoWithoutAccessToken(JwtToken jwtToken) {
        return JwtTokenDto.builder()
                .accessToken(jwtToken.getAccessToken())
                .build();
    }

    @Override
    public List<JwtTokenDto> convertToDto(List<JwtToken> jwtTokenList) {
        return null;
    }
}
