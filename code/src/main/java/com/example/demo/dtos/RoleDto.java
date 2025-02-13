package com.example.demo.dtos;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String name;
    private List<String> permissions;
}

