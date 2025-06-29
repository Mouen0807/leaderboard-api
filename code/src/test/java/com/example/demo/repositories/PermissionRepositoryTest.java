package com.example.demo.repositories;

import com.example.demo.models.Permission;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class PermissionRepositoryTest {
    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void testCreatePermission() {
        Permission permission = new Permission();
        permission.setName("testCreatePermission");
        permission.setDescription("testCreatePermission");

        Permission permissionCreated = permissionRepository.save(permission);

        Permission retrievedPermission = permissionRepository.findById(permissionCreated.getId()).orElse(null);

        assertNotNull(retrievedPermission);
        assertEquals("testCreatePermission", retrievedPermission.getName());
        assertEquals("testCreatePermission",retrievedPermission.getDescription());
    }

    @Test
    public void testGetPermission() {
        Permission permission = new Permission();
        permission.setName("testGetPermission");
        permission.setDescription("testGetPermissionDescription");

        Permission permissionCreated = permissionRepository.save(permission);

        Permission retrievedPermission = permissionRepository.findByName(permissionCreated.getName());

        assertNotNull(retrievedPermission);
        assertEquals("testGetPermission", retrievedPermission.getName());
        assertEquals("testGetPermissionDescription",retrievedPermission.getDescription());
    }

    @Test
    public void testUpdatePermission() {
        Permission permission = new Permission();
        permission.setName("Permission");
        permission.setDescription("testPermission");

        Permission permissionCreated = permissionRepository.save(permission);

        permissionCreated.setName("testUpdatedPermission");
        permissionCreated.setDescription("testUpdatedPermissionDescription");

        Permission permissionUpdated = permissionRepository.save(permission);

        Permission retrievedPermission = permissionRepository.findById(permissionUpdated.getId()).orElse(null);

        assertNotNull(retrievedPermission);
        assertEquals("testUpdatedPermission", retrievedPermission.getName());
        assertEquals("testUpdatedPermissionDescription",retrievedPermission.getDescription());
    }

    @Test
    public void testDeletePermission() {
        Permission permission = new Permission();
        permission.setName("testDeletePermission");
        permission.setDescription("testDeletePermission");

        Permission permissionCreated = permissionRepository.save(permission);
        permissionRepository.deleteById(permission.getId());

        Permission retrievedPermission = permissionRepository.findById(permissionCreated.getId()).orElse(null);

        assertNull(retrievedPermission);
    }
}
