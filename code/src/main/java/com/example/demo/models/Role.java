package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Permission> permissions;

    public List<String> getPermissionNames(){
        List<String> permissionNames = permissions.stream()
                .map(Permission::getName) // extract the name
                .collect(Collectors.toList());

        return permissionNames;
    }
}

