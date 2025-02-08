package com.example.demo.dtos;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private String name;
    private String description;
}

