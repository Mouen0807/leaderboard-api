package com.example.demo.services;

import com.example.demo.dtos.RoleDto;
import com.example.demo.mappers.RoleMapper;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleMapper roleMapper;

    @Test
    public void shouldCreateRoleWhenRoleDoesNotExistAndPermissionsAreValid() {
        // Given
        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role createdRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(permissionSet)
                .build();

        RoleDto expectedDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(null);

        Mockito.when(permissionRepository.findByName(permission1.getName()))
                .thenReturn(permission1);

        Mockito.when(permissionRepository.findByName(permission2.getName()))
                .thenReturn(permission2);

        Mockito.when(roleRepository.save(Mockito.any(Role.class)))
                .thenReturn(createdRole);

        Mockito.when(roleMapper.convertToDto(createdRole)).thenReturn(expectedDto);

        Optional<RoleDto> result = roleService.createRole(roleDto);

        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN", result.get().getName());
        Assertions.assertEquals(2, result.get().getPermissions().size());
        Assertions.assertEquals("permission1",result.get().getPermissions().get(0));

    }

    @Test
    public void shouldNotCreateRoleWhenPermissionsInValids() {
        // Given
        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(null);

        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Mockito.when(permissionRepository.findByName(permission1.getName()))
                .thenReturn(null);

        //Mockito.when(permissionRepository.findByName(permission2.getName()))
        //        .thenReturn(null);

        Optional<RoleDto> result = roleService.createRole(roleDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotCreateRoleWhenRoleExists() {
        // Given
        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role existingRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(permissionSet)
                .build();

        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(existingRole);

        Optional<RoleDto> result = roleService.createRole(roleDto);
        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateRoleWhenRoleIdNotExists() {
        Long idRole = 1L;

        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Mockito.when(roleRepository.findById(idRole)).thenReturn(Optional.empty());

        Optional<RoleDto> result = roleService.updateRole(idRole, roleDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateRoleWhenPermissionNotExists() {
        Long idRole = 1L;

        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("test", "permission2"))
                .build();

        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role createdRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(permissionSet)
                .build();

        Mockito.when(roleRepository.findById(idRole)).thenReturn(Optional.of(createdRole));

        Mockito.when(permissionRepository.findByName("test"))
                .thenReturn(null);

        Optional<RoleDto> result = roleService.updateRole(idRole, roleDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateRole() {
        Long idRole = 1L;

        RoleDto roleDto = new RoleDto();
        roleDto.setName("ADMIN");
        roleDto.setPermissions(List.of("permission3"));

        // existing
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role roleRetrieved = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        // updated

        Permission permission3 = Permission.builder()
                .id(3L)
                .name("permission3")
                .description("permission3")
                .build();

        Set<Permission> permissionSet2 = Set.of(permission3);

        Role roleUpdated = Role.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(permissionSet2)
                .build();

        RoleDto expectedDto = RoleDto.builder()
                .name("ADMIN")
                .permissions(List.of("permission3"))
                .build();

        Mockito.when(roleRepository.findById(idRole)).thenReturn(Optional.of(roleRetrieved));

        Mockito.when(permissionRepository.findByName("permission3"))
                .thenReturn(permission3);

        Mockito.when(roleRepository.save(Mockito.any(Role.class)))
                .thenReturn(roleUpdated);

        Mockito.when(roleMapper.convertToDto(roleUpdated)).thenReturn(expectedDto);


        Optional<RoleDto> result = roleService.updateRole(idRole, roleDto);


        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN", result.get().getName());
        Assertions.assertEquals(1, result.get().getPermissions().size());
        Assertions.assertEquals("permission3",result.get().getPermissions().get(0));
    }

    @Test
    public void shouldNotFindRole() {
        String roleName = "TEST";

        Mockito.when(roleRepository.findByName(roleName)).thenReturn(null);

        Optional<RoleDto> result = roleService.findRoleByName(roleName);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldFindRole() {
        String roleName = "ADMIN-TEST";
        // existing
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role roleRetrieved = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        RoleDto expectedDto = RoleDto.builder()
                .name("ADMIN-TEST")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Mockito.when(roleRepository.findByName(roleName)).thenReturn(roleRetrieved);

        Mockito.when(roleMapper.convertToDto(roleRetrieved)).thenReturn(expectedDto);

        Optional<RoleDto> result = roleService.findRoleByName(roleName);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getName());
        Assertions.assertEquals(2, result.get().getPermissions().size());
        Assertions.assertEquals("permission1",result.get().getPermissions().get(0));
    }

    @Test
    public void shouldFindEntityRole() {
        String roleName = "ADMIN-TEST";
        // existing
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role roleRetrieved = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        RoleDto expectedDto = RoleDto.builder()
                .name("ADMIN-TEST")
                .permissions(List.of("permission1", "permission2"))
                .build();

        Mockito.when(roleRepository.findByName(roleName)).thenReturn(roleRetrieved);

        Optional<Role> result = roleService.findEntityRoleByName(roleName);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getName());
        Assertions.assertEquals(2, result.get().getPermissions().size());
    }

    @Test
    public void shouldNotFindEntityRole() {
        String roleName = "TEST";

        Mockito.when(roleRepository.findByName(roleName)).thenReturn(null);

        Optional<Role> result = roleService.findEntityRoleByName(roleName);

        Assertions.assertTrue(result.isEmpty());
    }



}
