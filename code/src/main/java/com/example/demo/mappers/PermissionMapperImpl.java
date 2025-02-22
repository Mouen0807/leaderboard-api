package com.example.demo.mappers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionMapperImpl implements PermissionMapper{
    @Override
    public PermissionDto convertToDto(Permission permission) {
        if(permission == null) return null;

        PermissionDto permissionDto = PermissionDto.builder()
                .id(permission.getId())
                .description(permission.getDescription())
                .name(permission.getName())
                .build();

        return permissionDto;
    }

    @Override
    public List<PermissionDto> convertToDto(List<Permission> permissions) {
        if(permissions == null) return null;

        List<PermissionDto> permissionDtoList = new ArrayList<>();
        for(Permission permission: permissions){
            PermissionDto permissionDto = PermissionDto.builder()
                    .id(permission.getId())
                    .description(permission.getDescription())
                    .name(permission.getName())
                    .build();
            permissionDtoList.add(permissionDto);
        }

        return permissionDtoList;
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
