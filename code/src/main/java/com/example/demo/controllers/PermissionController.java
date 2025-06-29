package com.example.demo.controllers;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.PermissionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission/create")
    public ResponseEntity<?> createPermission(@Valid  @RequestBody PermissionDto permissionDto) {
        logger.info("Attempt to create permission {} ", permissionDto);

        Optional<PermissionDto> optPermissionDtoCreated = permissionService.createPermission(permissionDto);

        if(optPermissionDtoCreated.isEmpty()){
            ApiResponse<PermissionDto> apiResponse = ApiResponse.<PermissionDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Permission already exists")
                    .build();

            logger.info("Permission not created");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        logger.info("Permission created");
        ApiResponse<PermissionDto> apiResponse = ApiResponse.<PermissionDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Permission is created")
                .data(optPermissionDtoCreated.get())
                .build();

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/permission/all")
    public ResponseEntity<?> findAllPermissions() {
        logger.info("Attempt to retrieve all permissions");
        List<PermissionDto> permissionsDtoList  = permissionService.findAllPermissions();

        ApiResponse<List<PermissionDto> > apiResponse = ApiResponse.<List<PermissionDto> >builder()
            .code(HttpStatus.OK.toString())
            .message("All permissions retrieved")
            .data(permissionsDtoList)
            .build();

        logger.info("All permissions retrieved");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/permission/update/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable("id") Long id, @Valid @RequestBody PermissionDto permissionDto) {
        logger.info("Attempt to update permission id {} with values {}", id, permissionDto.toString());

        Optional<PermissionDto> optPermissionUpdated = permissionService.updatePermission(id, permissionDto);
        if(optPermissionUpdated.isEmpty()){
            ApiResponse<PermissionDto> apiResponse = ApiResponse.   <PermissionDto>builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Permission id not found or name already exists")
                    .build();

            logger.info("Permission id not found or name already exists");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        logger.info("Permission is updated");
        ApiResponse<PermissionDto> apiResponse = ApiResponse.<PermissionDto>builder()
                .code(HttpStatus.OK.toString())
                .message("Permission is updated")
                .data(optPermissionUpdated.get())
                .build();

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
