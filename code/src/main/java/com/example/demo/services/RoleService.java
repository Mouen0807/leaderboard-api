package com.example.demo.services;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.dtos.RoleDto;
import com.example.demo.mappers.RoleMapper;
import com.example.demo.mappers.RoleMapperImpl;
import com.example.demo.models.ApiResponse;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private static final RoleMapper roleMapper = new RoleMapperImpl();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public Optional<RoleDto> createRole(RoleDto roleDto) {
        try {
            logger.debug("Start creating role {} with permissions {}", roleDto.getName(), roleDto.getPermissions());

            Optional<Role> optRole = Optional.ofNullable(roleRepository.findByName(roleDto.getName()));
            if(optRole.isPresent()){
                logger.debug("Role already exists");
                return Optional.empty();
            }

            Optional<Set<Permission>> optSetPermission = checkPermissions(roleDto.getPermissions());
            if(optSetPermission.isEmpty()) return Optional.empty();

            Role role = Role.builder()
                    .name(roleDto.getName())
                    .permissions(optSetPermission.get())
                    .build();

            roleRepository.save(role);
            logger.debug("Role created");

            return Optional.of(roleMapper.convertToDto(role));
        } catch (Exception e) {
            logger.error("Failed to create role");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<RoleDto> updateRole(Long id, RoleDto roleDto) {
        try {
            logger.debug("Start updating role with id: {} ", roleDto.getId());

            Optional<Role> optRole = roleRepository.findById(id);
            if(optRole.isEmpty()) {
                logger.debug("Role not found");
                return Optional.empty();
            }

            Optional<Set<Permission>> optSetPermission = checkPermissions(roleDto.getPermissions());
            if(optSetPermission.isEmpty()) return Optional.empty();

            Role role = optRole.get();
            role.setPermissions(optSetPermission.get());
            role.setName(roleDto.getName());
            roleRepository.save(role);

            logger.debug("Role updated");
            return Optional.of(roleMapper.convertToDto(role));
        } catch (Exception e) {
            logger.error("Failed to update role");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<RoleDto> findRoleByName(String name){
        try {
            logger.debug("Start finding role by name: {} ", name);

            Optional<Role> optRole = Optional.ofNullable(roleRepository.findByName(name));
            if(optRole.isEmpty()){
                logger.debug("Role not found");
                return Optional.empty();
            }else{
                logger.debug("Role found");
                return Optional.of(roleMapper.convertToDto(optRole.get()));
            }
        } catch (Exception e) {
            logger.error("Failed to find role");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Role> findEntityRoleByName(String name){
        try {
            logger.debug("Start finding role entity by name: {} ", name);

            Optional<Role> optRole = Optional.ofNullable(roleRepository.findByName(name));
            if(optRole.isEmpty()){
                logger.debug("Role entity not found");
                return Optional.empty();
            }else{
                logger.debug("Role entity found");
                return optRole;
            }
        } catch (Exception e) {
            logger.error("Failed to find role entity");
            throw new RuntimeException(e.getMessage());
        }
    }


    public Optional<Set<Permission>> checkPermissions(List<String> permissions){
        Set<Permission> setPermissions = new HashSet<>();
        for(String permission: permissions){
            Optional<Permission> optPermission = Optional.ofNullable(permissionRepository.findByName(permission));

            if(optPermission.isEmpty()){
                logger.debug("Permission " + permission + " doesn't exist");
                return Optional.empty();
            }

            setPermissions.add(optPermission.get());
        }

        return Optional.of(setPermissions);
    }

    public Collection<GrantedAuthority> mapRolesToAuthorities(Role role) {
        List<String> permissionNames = role.getPermissions().stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .toList();

        return permissionNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
