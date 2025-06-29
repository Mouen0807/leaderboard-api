package com.example.demo.mappers;

import com.example.demo.dtos.RoleDto;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "permissionsToNames")
    public RoleDto convertToDto(Role role);
    public List<RoleDto> convertToDto(List<Role> roles);

    @Named("permissionsToNames")
    default List<String> mapPermissionsToNames(Set<Permission> permissions) {
        if (permissions == null) return null;
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toList());
    }
}
