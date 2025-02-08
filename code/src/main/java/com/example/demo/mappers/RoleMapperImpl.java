package com.example.demo.mappers;

import com.example.demo.dtos.RoleDto;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleMapperImpl implements RoleMapper{
    @Override
    public RoleDto convertToDto(Role role) {
        if(role == null) return null;

        List<String> permissionNames = role.getPermissions().stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .collect(Collectors.toList());

        RoleDto roleDto = RoleDto.builder()
                .name(role.getName())
                .permissions(permissionNames)
                .build();

        return roleDto;
    }

    @Override
    public List<RoleDto> convertToDto(List<Role> role) {
        return List.of();
    }
}
