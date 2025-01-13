package com.example.demo.mappers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.JwtToken;

import java.util.List;

public interface JwtTokenMapper {
    public JwtTokenDto convertToDto(JwtToken jwtToken);
    public List<JwtTokenDto> convertToDto(List<JwtToken> jwtTokenList);
}
