package com.example.demo.repositories;

import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void testCreateRole() {
        Role role = new Role();
        role.setName("testCreateRole");

        Set<Permission> setPermissions = new HashSet<>();

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permission1.setDescription("permission1");

        Permission permission2 = new Permission();
        permission2.setName("permission2");
        permission2.setDescription("permission2");

        Permission permission1Created = permissionRepository.save(permission1);
        Permission permission2Created = permissionRepository.save(permission2);

        setPermissions.add(permission1Created);
        setPermissions.add(permission2Created);

        role.setPermissions(setPermissions);
        Role roleCreated = roleRepository.save(role);

        Role retrievedRole = roleRepository.findById(roleCreated.getId()).orElse(null);

        assertNotNull(retrievedRole);
        assertEquals("testCreateRole", retrievedRole.getName());
        assertThat(retrievedRole.getPermissions()).hasSize(2);
        assertThat(retrievedRole.getPermissions()).contains(permission1Created, permission2Created);
    }


    @Test
    public void testGetRole() {
        Role role = new Role();
        role.setName("testGetRole");

        Set<Permission> setPermissions = new HashSet<>();

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permission1.setDescription("permission1");

        Permission permission2 = new Permission();
        permission2.setName("permission2");
        permission2.setDescription("permission2");

        Permission permission1Created = permissionRepository.save(permission1);
        Permission permission2Created = permissionRepository.save(permission2);

        setPermissions.add(permission1Created);
        setPermissions.add(permission2Created);

        role.setPermissions(setPermissions);
        Role roleCreated = roleRepository.save(role);

        Role retrievedRole = roleRepository.findByName(roleCreated.getName());

        assertNotNull(retrievedRole);
        assertEquals("testGetRole", retrievedRole.getName());
        assertThat(retrievedRole.getPermissions()).hasSize(2);
        assertThat(retrievedRole.getPermissions()).contains(permission1Created, permission2Created);
    }

    @Test
    public void testUpdateRole() {
        Role role = new Role();
        role.setName("testRole");

        Set<Permission> setPermissions = new HashSet<>();

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permission1.setDescription("permission1");

        Permission permission2 = new Permission();
        permission2.setName("permission2");
        permission2.setDescription("permission2");

        Permission permission1Created = permissionRepository.save(permission1);
        Permission permission2Created = permissionRepository.save(permission2);

        setPermissions.add(permission1Created);
        setPermissions.add(permission2Created);

        role.setPermissions(setPermissions);
        Role roleCreated = roleRepository.save(role);

        Permission permission3 = new Permission();
        permission3.setName("permission3");
        permission3.setDescription("permission3");

        Permission permission3Created = permissionRepository.save(permission3);
        setPermissions.add(permission3Created);

        roleCreated.setPermissions(setPermissions);
        roleCreated.setName("testUpdateRole");

        Role roleUpdated = roleRepository.save(roleCreated);

        Role retrievedRole = roleRepository.findById(roleUpdated.getId()).orElse(null);

        assertNotNull(retrievedRole);
        assertEquals("testUpdateRole", retrievedRole.getName());
        assertThat(retrievedRole.getPermissions()).hasSize(3);
        assertThat(retrievedRole.getPermissions()).contains(permission1Created, permission2Created, permission3Created);
    }

    @Test
    public void testDeleteRole() {
        Role role = new Role();
        role.setName("testDeleteRole");

        Set<Permission> setPermissions = new HashSet<>();

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permission1.setDescription("permission1");

        Permission permission1Created = permissionRepository.save(permission1);

        setPermissions.add(permission1Created);
        role.setPermissions(setPermissions);

        Role roleCreated = roleRepository.save(role);
        roleRepository.deleteById(roleCreated.getId());

        Role retrievedRole = roleRepository.findById(role.getId()).orElse(null);
        Permission retrievedPermission = permissionRepository.findById(permission1Created.getId()).orElse(null);

        assertNull(retrievedRole);
        assertNotNull(retrievedPermission);
    }

}

