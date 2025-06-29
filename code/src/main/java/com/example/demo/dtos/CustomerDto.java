package com.example.demo.dtos;

import com.example.demo.models.CustomerDetails;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CustomerDto {
    private String id;
    @NotBlank(message = "login must not be blank")
    private String login;
    private String password;
    private CustomerDetailsDto customerDetails;
    @NotBlank(message = "role must not be blank")
    private String role;
    private String createdAt;
    private String updatedAt;

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", customerDetails=" + customerDetails +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
