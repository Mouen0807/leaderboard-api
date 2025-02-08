package com.example.demo.mappers;

import com.example.demo.dtos.RoleDto;
import com.example.demo.models.Role;

import java.util.List;

public interface RoleMapper {
    public RoleDto convertToDto(Role role);
    public List<RoleDto> convertToDto(List<Role> role);
}
