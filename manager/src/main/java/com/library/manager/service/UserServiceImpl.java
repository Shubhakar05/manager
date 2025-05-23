package com.library.manager.service;

import com.library.manager.dto.UserDto;
import com.library.manager.entity.Role;
import com.library.manager.entity.User;
import com.library.manager.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(UserDto userDto) {
        // 1. Encode the password immediately
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        
        // 2. Create user entity
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword); // Store only encoded password
        user.setName(userDto.getName());
        user.setRole(userDto.getRole() != null ? userDto.getRole() : Role.STUDENT);
        
        // 3. Save to database
        User savedUser = userRepository.save(user);
        
        // 4. Return DTO without password
        UserDto responseDto = new UserDto();
        responseDto.setId(savedUser.getId());
        responseDto.setName(savedUser.getName());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setRole(savedUser.getRole());
        return responseDto;
    }

    // Keep all other existing methods exactly as is
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> {
                UserDto dto = new UserDto();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setRole(user.getRole());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
            .map(user -> {
                UserDto dto = new UserDto();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setRole(user.getRole());
                return dto;
            })
            .orElse(null);
    }
}