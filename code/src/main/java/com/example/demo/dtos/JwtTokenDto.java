package com.example.demo.dtos;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;

}
