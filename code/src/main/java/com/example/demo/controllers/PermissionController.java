package com.example.demo.controllers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    @Autowired
    private PermissionService permissionService;


    @PostMapping("/permission/create")
    public ResponseEntity<?> createPermission(@RequestBody PermissionDto permissionDto) {
        logger.info("Attempt to create permission with name: {} ", permissionDto.getName());
        Optional<PermissionDto> optPermissionDto  = permissionService.findPermissionByName(permissionDto.getName());

        if(optPermissionDto.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Permission already exist")
                    .build();

            logger.info("permission is not created");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        PermissionDto optPermissionDtoSaved = permissionService.createPermission(permissionDto);

        logger.info("permission is created");
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Permission is created")
                .data(optPermissionDtoSaved)
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }
}
