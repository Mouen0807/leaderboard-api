package com.example.demo.mappers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.Permission;

import java.util.List;

public interface PermissionMapper {
    public PermissionDto convertToDto(Permission permission);
    public List<PermissionDto> convertToDto(List<Permission> permission);
    public Permission convertToEntity(PermissionDto permissionDto);
}
