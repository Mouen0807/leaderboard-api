package com.example.demo.mappers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    public PermissionDto convertToDto(Permission permission);
    public List<PermissionDto> convertToDto(List<Permission> permissions);
    public Permission convertToEntity(PermissionDto permissionDto);
}
