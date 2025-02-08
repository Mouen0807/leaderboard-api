package com.example.demo.mappers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.Permission;

import java.util.List;

public class PermissionMapperImpl implements PermissionMapper{
    @Override
    public PermissionDto convertToDto(Permission permission) {
        if(permission == null) return null;

        PermissionDto permissionDto = PermissionDto.builder()
                .description(permission.getDescription())
                .name(permission.getName())
                .build();

        return permissionDto;
    }

    @Override
    public List<PermissionDto> convertToDto(List<Permission> permission) {
        return List.of();
    }

    @Override
    public Permission convertToEntity(PermissionDto permissionDto) {
        Permission permission = Permission.builder()
                .description(permissionDto.getDescription())
                .name(permissionDto.getName())
                .build();

        return permission;
    }
}
