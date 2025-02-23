package com.groupe3.pharmaconnect.security.utils;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import com.groupe3.pharmaconnect.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final AppUserService userService;

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AppUserDTO currentUser = (AppUserDTO) authentication.getPrincipal();
        return currentUser.getId().equals(userId);
    }
}