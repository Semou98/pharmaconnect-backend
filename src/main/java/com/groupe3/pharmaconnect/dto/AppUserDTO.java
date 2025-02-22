package com.groupe3.pharmaconnect.dto;

import com.groupe3.pharmaconnect.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime registrationDate;
}
