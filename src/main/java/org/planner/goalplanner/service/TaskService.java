package org.planner.goalplanner.service;

import jakarta.transaction.Transactional;
import org.planner.goalplanner.domain.Task;
import org.planner.goalplanner.dto.TaskDto;
import org.planner.goalplanner.dto.TodayTasksResponse;
import org.planner.goalplanner.enums.TaskStatus;
import org.planner.goalplanner.enums.TaskType;
import org.planner.goalplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TodayTasksResponse getTodayTasks(Long userId, String timezone) {
        ZoneId zoneId = (timezone != null && !timezone.isBlank())
                ? ZoneId.of(timezone)
                : ZoneId.systemDefault();   // fallback

        // Получаем сегодняшнюю дату по часовому поясу пользователя
        LocalDate today = ZonedDateTime.now(zoneId).toLocalDate();

        List<Task> tasks = taskRepository.findByUserIdAndPlannedDate(userId, today);
        List<TaskDto> mainTasks = tasks.stream()
                .filter(task -> task.getType() == TaskType.MAIN)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        List<TaskDto> sideTasks = tasks.stream()
                .filter(task -> task.getType() == TaskType.SIDE)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new TodayTasksResponse(mainTasks, sideTasks);
    }

    public void updateTaskStatus(Long taskId, TaskStatus newStatus, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        // Проверка владельца
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Доступ запрещён");
        }

        task.setStatus(newStatus);

        if (newStatus == TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDate.now());
        } else {
            task.setCompletedAt(null);
        }

        taskRepository.save(task);
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getType(),
                task.getStatus(),
                task.getPlannedDate()
        );
    }
}
