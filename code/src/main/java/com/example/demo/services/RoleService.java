package com.example.demo.services;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.dtos.RoleDto;
import com.example.demo.mappers.RoleMapper;
import com.example.demo.mappers.RoleMapperImpl;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            logger.debug("Start creating role by name: {} ", roleDto.getName());

            Set<Permission> setPermissions = new HashSet<>();
            for(String permission: roleDto.getPermissions()){
                Optional<Permission> optPermission = Optional.of(permissionRepository.findByName(permission));
                setPermissions.add(optPermission.get());
            }

            Role role = Role.builder()
                    .name(roleDto.getName())
                    .permissions(setPermissions)
                    .build();

            roleRepository.save(role);
            logger.debug("Role created");

            return Optional.ofNullable(roleMapper.convertToDto(role));
        } catch (Exception e) {
            logger.error("Failed to create role");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<RoleDto> updateRole(RoleDto roleDto) {
        try {
            logger.debug("Start updating role with id: {} ", roleDto.getId());

            Set<Permission> setPermissions = new HashSet<>();

            for(String permission: roleDto.getPermissions()){
                Optional<Permission> optPermission = Optional.of(permissionRepository.findByName(permission));
                setPermissions.add(optPermission.get());
            }

            Optional<Role> optRole = roleRepository.findById(roleDto.getId());
            if(optRole.isEmpty()) {
                logger.debug("Role not found");
                return Optional.empty();
            }

            Role role = optRole.get();
            role.setPermissions(setPermissions);
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
                return Optional.ofNullable(null);
            }else{
                logger.debug("Role found");
                return Optional.of(roleMapper.convertToDto(optRole.get()));
            }
        } catch (Exception e) {
            logger.error("Failed to find role");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<RoleDto> findRoleById(Long id){
        try {
            logger.debug("Start to find role with id: {} ", id);

            Optional<Role> optrole = roleRepository.findById(id);
            if(optrole.isEmpty()) {
                logger.debug("Role not found");
                return Optional.empty();
            }

            logger.debug("Role is found");
            return Optional.of(roleMapper.convertToDto(optrole.get()));
        } catch (Exception e) {
            logger.error("Failed to save permission");
            throw new RuntimeException(e.getMessage());
        }
    }
}
