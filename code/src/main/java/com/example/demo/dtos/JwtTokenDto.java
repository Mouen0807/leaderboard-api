package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDto {
    @NotBlank(message = "accessToken must not be blank")
    private String accessToken;

    @NotBlank(message = "refreshTokn must not be blank")
    private String refreshToken;

    @Override
    public String toString() {
        return "JwtTokenDto{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
