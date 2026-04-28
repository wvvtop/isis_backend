package org.planner.goalplanner.service;

import jakarta.transaction.Transactional;
import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.RegistrationRequestDto;
import org.planner.goalplanner.dto.UserResponseDto;
import org.planner.goalplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto register(RegistrationRequestDto registrationDto) {
        // Проверяем, существует ли пользователь с таким email
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        // Создание нового пользователя
        User user = new User();

        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser);
    }
}
