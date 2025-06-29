package com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsDto {
    private String id;

    @NotBlank(message = "firstName must not be blank")
    private String firstName;

    @NotBlank(message = "secondName must not be blank")
    private String secondName;

    @NotBlank(message = "birthdate must not be blank")
    private String dateOfBirth;

    @NotBlank(message = "phoneNumber must not be blank")
    private String phoneNumber;

    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "origin country must not be blank")
    private String originCountry;

    @Override
    public String toString() {
        return "CustomerDetailsDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", originCountry='" + originCountry + '\'' +
                '}';
    }
}
