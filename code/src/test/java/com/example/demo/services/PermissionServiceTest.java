package com.example.demo.services;

import com.example.demo.dtos.PermissionDto;
import com.example.demo.mappers.PermissionMapper;
import com.example.demo.models.Permission;
import com.example.demo.repositories.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @Test
    public void shouldReturnPermissionWhenPermissionIdExists(){
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("per1");
        permission.setDescription("per1");

        Optional<Permission> optPermission = Optional.of(permission);

        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(1L);
        permissionDto.setName("per1");
        permissionDto.setDescription("per1");

        Mockito.when(permissionRepository.findById(1L))
                .thenReturn(optPermission);

        Mockito.when(permissionMapper.convertToDto(optPermission.get()))
                .thenReturn(permissionDto);

        Optional<PermissionDto> result = permissionService.findPermissionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("per1",result.get().getName());
        assertEquals("per1",result.get().getDescription());
    }


    @Test
    public void shouldReturnEmptyOptionalWhenPermissionIdNotExist(){
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("per1");
        permission.setDescription("per1");

        Mockito.when(permissionRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<PermissionDto> result = permissionService.findPermissionById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnAllPermissionsWhenPermissionsExist(){
        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("per1");
        permission1.setDescription("per1");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("per2");
        permission2.setDescription("per2");

        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission1);
        permissions.add(permission2);

        PermissionDto permissionDto1 = new PermissionDto();
        permissionDto1.setId(1L);
        permissionDto1.setName("per1");
        permissionDto1.setDescription("per1");

        PermissionDto permissionDto2 = new PermissionDto();
        permissionDto2.setId(2L);
        permissionDto2.setName("per2");
        permissionDto2.setDescription("per2");

        List<PermissionDto> permissionDtos = new ArrayList<>();
        permissionDtos.add(permissionDto1);
        permissionDtos.add(permissionDto2);

        Mockito.when(permissionRepository.findAll())
                .thenReturn(permissions);

        Mockito.when(permissionMapper.convertToDto(permissions))
                .thenReturn(permissionDtos);

        List<PermissionDto> results = permissionService.findAllPermissions();

        assertEquals(2, results.size());
        assertEquals("per1", results.get(0).getName());
        assertEquals("per1", results.get(0).getDescription());
        assertEquals("per2", results.get(1).getName());
        assertEquals("per2", results.get(1).getDescription());
    }

    @Test
    public void shouldNotCreatePermissionWhenPermissionAlreadyExists(){
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("per1");
        permissionDto.setDescription("per1");

        Permission permission = new Permission();
        permission.setName("per1");
        permission.setDescription("per1");

        Mockito.when(permissionRepository.findByName(permissionDto.getName()))
                .thenReturn(permission);

        Optional<PermissionDto> result = permissionService.createPermission(permissionDto);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldCreatePermissionWhenPermissionNotExist(){
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("per1");
        permissionDto.setDescription("per1");

        Permission permissionToSave = new Permission();
        permissionToSave.setName("per1");
        permissionToSave.setDescription("per1");

        Permission permissionSaved = new Permission();
        permissionSaved.setId(1L);
        permissionSaved.setName("per1");
        permissionSaved.setDescription("per1");

        PermissionDto permissionSavedToDto = new PermissionDto();
        permissionSavedToDto.setId(1L);
        permissionSavedToDto.setName("per1");
        permissionSavedToDto.setDescription("per1");

        Mockito.when(permissionRepository.findByName(permissionDto.getName()))
                .thenReturn(null);

        Mockito.when(permissionMapper.convertToEntity(permissionDto))
                .thenReturn(permissionToSave);

        Mockito.when(permissionRepository.save(permissionToSave))
                .thenReturn(permissionSaved);

        Mockito.when(permissionMapper.convertToDto(permissionSaved))
                .thenReturn(permissionSavedToDto);

        Optional<PermissionDto> result = permissionService.createPermission(permissionDto);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("per1",result.get().getName());
        assertEquals("per1",result.get().getDescription());
    }

    @Test
    public void shouldNotUpdatePermissionWhenPermissionIdNotExists(){
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("per2");
        permissionDto.setDescription("per2");

        Permission permissionToUpdate = new Permission();
        permissionToUpdate.setId(1L);
        permissionToUpdate.setName("per1");
        permissionToUpdate.setDescription("per1");

        Mockito.when(permissionRepository.findById(permissionToUpdate.getId()))
                .thenReturn(Optional.empty());

        Optional<PermissionDto> result = permissionService.updatePermission(permissionToUpdate.getId(),permissionDto);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdatePermission(){
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("per1");
        permissionDto.setDescription("per1");

        Permission permissionToUpdate = new Permission();
        permissionToUpdate.setId(1L);
        permissionToUpdate.setName("per2");
        permissionToUpdate.setDescription("per2");

        Permission permissionUpdated = new Permission();
        permissionUpdated.setId(1L);
        permissionUpdated.setName("per1");
        permissionUpdated.setDescription("per1");

        PermissionDto permissionUpdatedDto = new PermissionDto();
        permissionUpdatedDto.setId(1L);
        permissionUpdatedDto.setName("per1");
        permissionUpdatedDto.setDescription("per1");

        Optional<Permission> opt = Optional.of(permissionToUpdate);

        Mockito.when(permissionRepository.findById(permissionToUpdate.getId()))
                .thenReturn(opt);

        Mockito.when(permissionRepository.save(opt.get()))
                .thenReturn(permissionUpdated);

        Mockito.when(permissionMapper.convertToDto(permissionUpdated))
                .thenReturn(permissionUpdatedDto);

        Optional<PermissionDto> result = permissionService.updatePermission(permissionToUpdate.getId(),permissionDto);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("per1",result.get().getName());
        assertEquals("per1",result.get().getDescription());
    }
}
