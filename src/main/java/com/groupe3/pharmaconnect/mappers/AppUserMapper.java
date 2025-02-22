package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import com.groupe3.pharmaconnect.entities.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {
    public AppUserDTO toDTO(AppUser user) {
        if (user == null) return null;

        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }

    public AppUser toEntity(AppUserDTO dto) {
        if (dto == null) return null;

        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setRegistrationDate(dto.getRegistrationDate());
        return user;
    }
}
