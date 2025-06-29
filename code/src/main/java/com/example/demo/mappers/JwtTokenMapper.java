package com.example.demo.mappers;

import com.example.demo.dtos.JwtTokenDto;
import com.example.demo.models.JwtToken;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JwtTokenMapper {
    public JwtTokenDto convertToDto(JwtToken jwtToken);
    public List<JwtTokenDto> convertToDto(List<JwtToken> jwtTokenList);
}
