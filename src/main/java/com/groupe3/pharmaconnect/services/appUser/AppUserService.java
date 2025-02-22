package com.groupe3.pharmaconnect.services.appUser;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppUserService {
    AppUserDTO createUser(AppUserDTO userDTO);
    AppUserDTO updateUser(Long id, AppUserDTO userDTO);
    void deleteUser(Long id);
    AppUserDTO getUserById(Long id);
    AppUserDTO getUserByEmail(String email);
    Page<AppUserDTO> getAllUsers(Pageable pageable);
    boolean existsByEmail(String email);
}