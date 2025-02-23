package com.groupe3.pharmaconnect.security.payloads;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private AppUserDTO user;
}