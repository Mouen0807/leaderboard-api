package com.example.demo.dtos;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private String name;
    private String description;
}

