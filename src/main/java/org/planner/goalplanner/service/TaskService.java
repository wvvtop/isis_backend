package org.planner.goalplanner.service;

import jakarta.transaction.Transactional;
import org.planner.goalplanner.domain.Milestone;
import org.planner.goalplanner.domain.Task;
import org.planner.goalplanner.dto.task.TaskDto;
import org.planner.goalplanner.dto.task.TaskPatchDto;
import org.planner.goalplanner.dto.task.TodayTasksResponse;
import org.planner.goalplanner.enums.TaskStatus;
import org.planner.goalplanner.enums.TaskType;
import org.planner.goalplanner.repository.MilestoneRepository;
import org.planner.goalplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    private final MilestoneRepository milestoneRepository;

    public TaskService(TaskRepository taskRepository, MilestoneRepository milestoneRepository) {
        this.taskRepository = taskRepository;
        this.milestoneRepository = milestoneRepository;
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


    public void patchTask(Long taskId, Long userId, TaskPatchDto taskPatchDto) {
        Task task = taskRepository.findByIdIncludingDeletedNative(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        // Проверка владельца
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Доступ запрещён");
        }

        if(taskPatchDto.getDeleted() != null)
            taskRepository.restoreById(taskId);

        if((taskPatchDto.getTitle() != null && !taskPatchDto.getTitle().isEmpty()))
            task.setTitle(taskPatchDto.getTitle());
        if(taskPatchDto.getTaskType() != null)
            task.setType(taskPatchDto.getTaskType());
        if(taskPatchDto.getTaskStatus() != null)
            task.setStatus(taskPatchDto.getTaskStatus());
        if(taskPatchDto.getPlannedDate() != null) {
//            if(taskPatchDto.getTaskType() == TaskType.MAIN && task.getUser().getMax_main_tasks_per_day() < 3 &&
//                task.getMilestone()){
//
//            }
        }
            task.setPlannedDate(taskPatchDto.getPlannedDate());
        if(taskPatchDto.getMilestoneId() != null) {
            Milestone milestone = milestoneRepository.findById(taskPatchDto.getMilestoneId())
                    .orElseThrow(() -> new RuntimeException("Этап не найден"));
            // важно!
            if (!milestone.getGoal().getId().equals(task.getGoal().getId())) {
                throw new RuntimeException("Ошибка: этап из другой цели");
            }

            task.setMilestone(milestone);
        }

        taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        // Проверка владельца
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Доступ запрещён");
        }

        taskRepository.delete(task);
    }

    public List<TaskDto> getDeletedTasks(Long userId, Long goalId) {
        List<Task> tasks = taskRepository.findDeletedByUserIdAndGoalId(userId, goalId);
        List<TaskDto> deletedTasks = tasks.stream()
                .filter(task -> task.getType() == TaskType.MAIN)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return deletedTasks;
    }
}
