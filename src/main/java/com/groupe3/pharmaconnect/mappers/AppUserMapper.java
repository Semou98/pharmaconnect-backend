package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import com.groupe3.pharmaconnect.entities.AppUser;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppUserMapper {

    public AppUserDTO toDTO(AppUser user) {
        if (user == null) return null;

        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        dto.setRegistrationDate(user.getRegistrationDate());

        // Ne pas inclure le mot de passe dans le DTO pour des raisons de sécurité
        // Mapper uniquement les IDs des pharmacies associées
        dto.setPharmacyIds(user.getPharmacies().stream()
                .map(Pharmacy::getId)
                .collect(Collectors.toSet()));

        return dto;
    }

    public AppUser toEntity(AppUserDTO dto) {
        if (dto == null) return null;

        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRoles(dto.getRoles());
        user.setRegistrationDate(dto.getRegistrationDate());
        // Le mot de passe doit être géré séparément avec un encodage approprié

        return user;
    }
}