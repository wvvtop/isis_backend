package org.planner.goalplanner.controller;

import jakarta.validation.Valid;
import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.CreateGoalRequest;
import org.planner.goalplanner.dto.GoalListDto;
import org.planner.goalplanner.repository.UserRepository;
import org.planner.goalplanner.service.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserRepository userRepository;

    // Конструктор вместо @RequiredArgsConstructor
    public GoalController(GoalService goalService, UserRepository userRepository) {
        this.goalService = goalService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGoal(
            @Valid @RequestBody CreateGoalRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Пользователь не авторизован");
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        Long goalId = goalService.createGoal(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(goalId);
    }

    @GetMapping
    public ResponseEntity<List<GoalListDto>> getAllGoals(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        List<GoalListDto> goals = goalService.getAllGoals(userId);
        return ResponseEntity.ok(goals);
    }
}
