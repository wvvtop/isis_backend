package org.planner.goalplanner.service;

import jakarta.transaction.Transactional;
import org.planner.goalplanner.domain.Goal;
import org.planner.goalplanner.domain.Milestone;
import org.planner.goalplanner.domain.Task;
import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.*;
import org.planner.goalplanner.dto.goal.CreateGoalRequest;
import org.planner.goalplanner.dto.goal.DailyTasksDto;
import org.planner.goalplanner.dto.goal.GoalDto;
import org.planner.goalplanner.dto.goal.GoalListDto;
import org.planner.goalplanner.enums.TaskStatus;
import org.planner.goalplanner.enums.TaskType;
import org.planner.goalplanner.mapper.GoalMapper;
import org.planner.goalplanner.repository.GoalRepository;
import org.planner.goalplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    private final GoalMapper goalMapper;

    // Конструктор вместо @RequiredArgsConstructor
    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
    }

    public Long createGoal(CreateGoalRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());

        if (request.getMilestones() != null && !request.getMilestones().isEmpty()) {
            for (int i = 0; i < request.getMilestones().size(); i++) {
                CreateMilestoneRequest mReq = request.getMilestones().get(i);

                Milestone milestone = new Milestone();
                milestone.setGoal(goal);
                milestone.setTitle(mReq.getTitle());
                milestone.setDescription(mReq.getDescription());
                milestone.setOrder(i + 1);
                milestone.setStartDate(mReq.getStartDate());
                milestone.setEndDate(mReq.getEndDate());
                goal.getMilestones().add(milestone);

                // Создаём задачи для этого этапа
                createTasksFromDailyData(milestone, mReq.getTasks(), user, goal);
            }
        }

        Goal savedGoal = goalRepository.save(goal);
        return savedGoal.getId();
    }

    private void createTasksFromDailyData(Milestone milestone,
                                          Map<String, DailyTasksDto> dailyTasks,
                                          User user, Goal goal) {

        if (dailyTasks == null || dailyTasks.isEmpty()) {
            return;
        }

        for (Map.Entry<String, DailyTasksDto> entry : dailyTasks.entrySet()) {
            String dateStr = entry.getKey();
            DailyTasksDto tasksDto = entry.getValue();
            LocalDate plannedDate = LocalDate.parse(dateStr);

            // Main задачи
            if (tasksDto.getMain() != null) {
                for (String title : tasksDto.getMain()) {
                    if (title == null || title.trim().isEmpty()) continue;
                    Task task = createSingleTask(title, TaskType.MAIN, plannedDate, user, goal, milestone);
                    milestone.getTasks().add(task);
                }
            }

            // Side задачи
            if (tasksDto.getSide() != null) {
                for (String title : tasksDto.getSide()) {
                    if (title == null || title.trim().isEmpty()) continue;
                    Task task = createSingleTask(title, TaskType.SIDE, plannedDate, user, goal, milestone);
                    milestone.getTasks().add(task);
                }
            }
        }
    }

    private Task createSingleTask(String title, TaskType type, LocalDate plannedDate,
                                  User user, Goal goal, Milestone milestone) {
        Task task = new Task();
        task.setUser(user);
        task.setGoal(goal);
        task.setMilestone(milestone);
        task.setTitle(title.trim());
        task.setType(type);
        task.setStatus(TaskStatus.PENDING);
        task.setPlannedDate(plannedDate);
        return task;
    }

    public GoalDto getGoalWithMilestones(Long userId, Long goalId) {
        Optional<Goal> goal = goalRepository.findByUserIdAndId(userId, goalId);
        System.out.println(goal.get().getId()   );
        if(goal.isEmpty())
            return null;

        GoalDto goalDto = goalMapper.mappingToGoalDto(goal.get());

        return goalDto;
    }



    public List<GoalListDto> getAllGoals(Long userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);

        return goals.stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    private GoalListDto convertToListDto(Goal goal) {
        int milestonesCount = goal.getMilestones() != null ? goal.getMilestones().size() : 0;

        // Простой расчёт прогресса (можно улучшить позже)
        double progress = 0.0;
        if (milestonesCount > 0) {
            long completedTasks = goal.getMilestones().stream()
                    .flatMap(m -> m.getTasks().stream())
                    .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                    .count();

            long totalTasks = goal.getMilestones().stream()
                    .flatMap(m -> m.getTasks().stream())
                    .count();

            progress = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0;
        }

        String status = "ACTIVE";
        if (goal.getEndDate() != null && goal.getEndDate().isBefore(LocalDate.now())) {
            status = "COMPLETED";
        }

        return new GoalListDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getStartDate(),
                goal.getEndDate(),
                milestonesCount,
                Math.round(progress * 10.0) / 10.0,   // округляем до 1 знака
                status
        );
    }
}
