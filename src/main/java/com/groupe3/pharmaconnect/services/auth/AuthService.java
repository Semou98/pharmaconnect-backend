package com.groupe3.pharmaconnect.services.auth;

import com.groupe3.pharmaconnect.security.payloads.AuthRequest;
import com.groupe3.pharmaconnect.security.payloads.AuthResponse;
import com.groupe3.pharmaconnect.security.payloads.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    void logout(String token);  // Optional: if you want to implement token blacklisting
}