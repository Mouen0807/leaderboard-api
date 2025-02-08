package com.example.demo.services;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.mappers.PermissionMapper;
import com.example.demo.mappers.PermissionMapperImpl;
import com.example.demo.models.Permission;
import com.example.demo.repositories.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    private static final PermissionMapper permissionMapper = new PermissionMapperImpl();

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> findAllPermissions(){
        return permissionRepository.findAll();
    }

    public PermissionDto createPermission(PermissionDto permissionDto){
        try {
            logger.debug("Start saving permission with name: {} ", permissionDto.getName());

            Permission permissionToSaved = permissionMapper.convertToEntity(permissionDto);
            Permission permissionSaved = permissionRepository.save(permissionToSaved);

            logger.debug("Permission is saved");
            return permissionMapper.convertToDto(permissionSaved);
        } catch (Exception e) {
            logger.error("Failed to save permission");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<PermissionDto> findPermissionByName(String name){
        try {
            logger.debug("Start finding permission by name: {} ", name);

            Permission permission = permissionRepository.findByName(name);

            logger.debug("Permission found");
            return Optional.ofNullable(permissionMapper.convertToDto(permission));
        } catch (Exception e) {
            logger.error("Failed to finds permission");
            throw new RuntimeException(e.getMessage());
        }
    }
}
