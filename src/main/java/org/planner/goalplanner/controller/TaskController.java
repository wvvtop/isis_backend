package org.planner.goalplanner.controller;

import org.planner.goalplanner.dto.StatusUpdateRequest;
import org.planner.goalplanner.dto.task.TaskDto;
import org.planner.goalplanner.dto.task.TaskPatchDto;
import org.planner.goalplanner.dto.task.TodayTasksResponse;
import org.planner.goalplanner.repository.UserRepository;
import org.planner.goalplanner.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("{taskId}/patch")
    public ResponseEntity<?> patchTask(
            @PathVariable Long taskId,
            @RequestBody TaskPatchDto taskPatchDto,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Пользователь не авторизован");
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        taskService.patchTask(taskId, userId, taskPatchDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{taskId}/delete")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication
    )
    {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Пользователь не авторизован");
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        taskService.deleteTask(taskId, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{goalId}/get-deleted")
    public ResponseEntity<List<TaskDto>> getDeletedTasksFromGoal(
            @PathVariable Long goalId,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();

        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        List<TaskDto> deletedTasks = taskService.getDeletedTasks(userId, goalId);

        return ResponseEntity.ok(deletedTasks);
    }
}
