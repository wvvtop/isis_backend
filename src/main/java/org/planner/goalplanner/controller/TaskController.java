package org.planner.goalplanner.controller;

import org.planner.goalplanner.dto.StatusUpdateRequest;
import org.planner.goalplanner.dto.TodayTasksResponse;
import org.planner.goalplanner.repository.UserRepository;
import org.planner.goalplanner.security.UserPrincipal;
import org.planner.goalplanner.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayTasks(
            Authentication authentication,
            @RequestHeader(value = "X-Timezone", required = false) String timezone) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Пользователь не авторизован");
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        TodayTasksResponse response = taskService.getTodayTasks(userId, timezone);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody StatusUpdateRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        taskService.updateTaskStatus(taskId, request.getStatus(), userId);
        return ResponseEntity.ok().build();
    }
}
