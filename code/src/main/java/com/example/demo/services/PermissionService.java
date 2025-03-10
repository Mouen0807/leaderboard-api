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

    public List<PermissionDto> findAllPermissions(){
        try{
            List<PermissionDto> permissionsDtos = permissionMapper.convertToDto(permissionRepository.findAll());

            logger.debug("Permissions found");
            return permissionsDtos;
        } catch (Exception e) {
            logger.error("Failed to find permission");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<PermissionDto> createPermission(PermissionDto permissionDto){
        try {
            logger.debug("Start saving permission with name: {} ", permissionDto.getName());

            Optional<Permission> optPermission = Optional.ofNullable(permissionRepository.findByName(permissionDto.getName()));
            if(optPermission.isPresent()){
                logger.debug("Permission already exists");
                return Optional.empty();
            }

            Permission permissionToSaved = permissionMapper.convertToEntity(permissionDto);
            Permission permissionSaved = permissionRepository.save(permissionToSaved);

            logger.debug("Permission is saved");
            return Optional.of(permissionMapper.convertToDto(permissionSaved));
        } catch (Exception e) {
            logger.error("Failed to save permission");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<PermissionDto> updatePermission(Long id, PermissionDto permissionDto){
        try {
            logger.debug("Start to update permission id {} ", permissionDto.getId());

            Optional<Permission> optPermission = permissionRepository.findById(id);
            if(optPermission.isEmpty()) {
                logger.debug("Permission not found");
                return Optional.empty();
            }

            Optional<Permission> optCheckName = Optional.ofNullable(permissionRepository.findByName(permissionDto.getName()));
            if(optCheckName.isEmpty()){
                logger.debug("Permission name already exists");
                return Optional.empty();
            }

            Permission permissionToUpdate = optPermission.get();
            permissionToUpdate.setDescription(permissionDto.getDescription());
            permissionToUpdate.setName(permissionDto.getName());
            Permission permissionSaved = permissionRepository.save(permissionToUpdate);

            logger.debug("Permission is updated");
            return Optional.of(permissionMapper.convertToDto(permissionSaved));
        } catch (Exception e) {
            logger.error("Failed to save permission");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<PermissionDto> findPermissionById(Long id){
        try {
            logger.debug("Start to find permission with id: {} ", id);

            Optional<Permission> optPermission = permissionRepository.findById(id);
            if(optPermission.isEmpty()) {
                logger.debug("Permission not found");
                return Optional.empty();
            }

            logger.debug("Permission is found");
            return Optional.of(permissionMapper.convertToDto(optPermission.get()));
        } catch (Exception e) {
            logger.error("Failed to save permission");
            throw new RuntimeException(e.getMessage());
        }
    }
}
