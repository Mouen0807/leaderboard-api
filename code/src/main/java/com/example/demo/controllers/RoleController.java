package com.example.demo.controllers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.dtos.RoleDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.models.Permission;
import com.example.demo.services.PermissionService;
import com.example.demo.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/role/create")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        logger.info("Attempt to create role with name: {} ", roleDto.getName());
        Optional<RoleDto> optRoleDto  = roleService.findRoleByName(roleDto.getName());

        if(optRoleDto.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role already exists")
                    .build();

            logger.info("Role is not created");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        List<String> permissions = roleDto.getPermissions();

        for(String permission: permissions){
            Optional<PermissionDto> optPermission = permissionService.findPermissionByName(permission);
            if(optPermission.isEmpty()){
                ApiResponse apiResponse = ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .message("permission " + permission + " doesn't exist")
                        .build();

                logger.info("permission doesn't exist");
                return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
            }
        }

        Optional<RoleDto> optRoleDtoSaved = roleService.createRole(roleDto);

        logger.info("role is created");
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Role is created")
                .data(optRoleDtoSaved)
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<?> findRoleByName(@RequestParam String name) {
        logger.info("Attempt to find role with name: {} ", name);
        Optional<RoleDto> optRoleDto  = roleService.findRoleByName(name);

        if(optRoleDto.isEmpty()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.NOT_FOUND.toString())
                    .message("Role is not found")
                    .build();

            logger.info("Role is not found");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
        }

        logger.info("Role is successfully found");
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Role is found")
                .data(optRoleDto.get())
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/role/update/{id}")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto, @PathVariable Long id) {
        logger.info("Attempt to update role with id {}", id);
        roleDto.setId(id);

        Optional<RoleDto> optRoleDto  = roleService.findRoleById(id);

        if(optRoleDto.isEmpty()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role doesn't exist")
                    .build();

            logger.info("Role doesn't exist");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        List<String> permissions = roleDto.getPermissions();

        for(String permission: permissions){
            Optional<PermissionDto> optPermission = permissionService.findPermissionByName(permission);
            if(optPermission.isEmpty()){
                ApiResponse apiResponse = ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .message("Permission " + permission + " doesn't exist")
                        .build();

                logger.info("Permission doesn't exist");
                return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
            }
        }

        Optional<RoleDto> optRoleDtoUpdated = roleService.updateRole(roleDto);

        logger.info("Role is updated");
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Role is updated")
                .data(optRoleDtoUpdated.get())
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }
}
