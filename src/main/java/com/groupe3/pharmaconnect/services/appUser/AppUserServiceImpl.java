package com.groupe3.pharmaconnect.services.appUser;

import com.groupe3.pharmaconnect.dto.AppUserDTO;
import com.groupe3.pharmaconnect.entities.AppUser;
import com.groupe3.pharmaconnect.mappers.AppUserMapper;
import com.groupe3.pharmaconnect.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDTO getUserByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'email : " + email));
        return userMapper.toDTO(user);
    }

    @Override
    public AppUserDTO createUser(AppUserDTO userDTO) {
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        AppUser user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Autres implémentations des méthodes de l'interface...
    @Override
    public AppUserDTO updateUser(Long id, AppUserDTO userDTO) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'id : " + id));
        // Mise à jour des champs
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public AppUserDTO getUserById(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public Page<AppUserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }
}