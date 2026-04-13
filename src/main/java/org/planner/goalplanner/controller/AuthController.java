package org.planner.goalplanner.controller;


import jakarta.validation.Valid;
import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.LoginDto;
import org.planner.goalplanner.dto.RegistrationRequestDto;
import org.planner.goalplanner.dto.UserResponseDto;
import org.planner.goalplanner.service.JwtService;
import org.planner.goalplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequestDto dto){
        UserResponseDto user = userService.register(dto);

        String token = jwtService.generateToken(dto.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Регистрация успешна!",
                "token", token,
                "email", user.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        // Используем AuthenticationManager для проверки email + пароль
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Если дошли сюда — значит логин успешен
        String token = jwtService.generateToken(dto.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Успешный вход"
        ));
    }
}
