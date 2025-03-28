package com.example.demo.repositories;

import com.example.demo.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    public Permission findByName(String name);
    public List<Permission> findAll();
}
