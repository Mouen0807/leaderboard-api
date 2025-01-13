package com.example.demo.dtos;

import com.example.demo.models.CustomerDetails;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CustomerLoginDto {
    private String id;
    private String login;
    private CustomerDetails customerDetails;
    private String role;
    private List<String> permissions;
    private String createdAt;
    private String updatedAt;
}
