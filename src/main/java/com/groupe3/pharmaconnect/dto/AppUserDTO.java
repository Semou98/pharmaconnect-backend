package com.groupe3.pharmaconnect.dto;

import com.groupe3.pharmaconnect.enums.AppUserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AppUserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Set <AppUserRole> roles;
    private LocalDateTime registrationDate;
    Set<Long> pharmacyIds;

}
