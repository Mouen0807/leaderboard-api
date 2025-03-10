package com.example.demo.controllers;

import com.example.demo.dtos.RoleDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @PostMapping("/role/create")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        logger.info("Attempt to create role {}", roleDto.toString());

        Optional<RoleDto> optRoleDtoCreated = roleService.createRole(roleDto);

        if(optRoleDtoCreated.isEmpty()){
            ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role already exists or one of permission is not found")
                    .build();

            logger.info("Role is not created");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        logger.info("role is created");
        ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Role is created")
                .data(optRoleDtoCreated.get())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<?> findRoleByName(@RequestParam String name) {
        logger.info("Attempt to find role with name: {} ", name);

        Optional<RoleDto> optRoleDto  = roleService.findRoleByName(name);
        if(optRoleDto.isEmpty()){
            ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                    .code(HttpStatus.NOT_FOUND.toString())
                    .message("Role is not found")
                    .build();

            logger.info("Role is not found");
            return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
        }

        logger.info("Role is successfully found");
        ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Role is found")
                .data(optRoleDto.get())
                .build();

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    
    @PutMapping("/role/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        logger.info("Attempt to update role id {} with values {}", id, roleDto.toString());

        Optional<RoleDto> optRoleDtoUpdated = roleService.updateRole(id, roleDto);
        if(optRoleDtoUpdated.isEmpty()){
            ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Role doesn't exist or one of permission is not found")
                    .build();

            logger.info("Role not updated");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        logger.info("Role is updated");
        ApiResponse<RoleDto> apiResponse = ApiResponse.<RoleDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Role is updated")
                .data(optRoleDtoUpdated.get())
                .build();

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
