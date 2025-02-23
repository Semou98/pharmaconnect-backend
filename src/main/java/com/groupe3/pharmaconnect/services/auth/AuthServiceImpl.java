package com.groupe3.pharmaconnect.services.auth;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import com.groupe3.pharmaconnect.exceptions.InvalidCredentialsException;
import com.groupe3.pharmaconnect.exceptions.UserAlreadyExistsException;
import com.groupe3.pharmaconnect.mappers.AppUserMapper;
import com.groupe3.pharmaconnect.security.jwt.JwtUtil;
import com.groupe3.pharmaconnect.security.payloads.AuthRequest;
import com.groupe3.pharmaconnect.security.payloads.AuthResponse;
import com.groupe3.pharmaconnect.security.payloads.RegisterRequest;
import com.groupe3.pharmaconnect.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserService userService;
    private final AppUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userService.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Create new user
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(passwordEncoder.encode(request.getPassword()));
        userDTO.setRoles(request.getRoles());

        AppUserDTO savedUser = userService.createUser(userDTO);
        String token = jwtUtil.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .user(savedUser)
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUserDTO userDTO = userService.getUserByEmail(request.getEmail());
            String token = jwtUtil.generateToken(userDTO);

            return AuthResponse.builder()
                    .token(token)
                    .user(userDTO)
                    .build();
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    public void logout(String token) {
        // Implement token blacklisting if needed
        SecurityContextHolder.clearContext();
    }
}