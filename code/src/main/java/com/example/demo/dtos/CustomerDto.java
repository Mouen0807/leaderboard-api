package com.example.demo.dtos;

import com.example.demo.models.CustomerDetails;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CustomerDto {
    private String id;
    private String login;
    private String password;
    private CustomerDetails customerDetails;
    private String role;
    private String createdAt;
    private String updatedAt;
}
