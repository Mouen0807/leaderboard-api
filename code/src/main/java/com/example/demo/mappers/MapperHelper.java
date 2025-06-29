package com.example.demo.mappers;

import com.example.demo.models.Role;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MapperHelper {
    @Named("mapRoleToString")
    public static String mapRoleToString(Role role) {
        return role != null ? role.getName() : null;
    }

    @Named("uuidToString")
    public String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    public UUID stringToUuid(String id) {
        return id != null && !id.isBlank() ? UUID.fromString(id) : null;
    }
}
