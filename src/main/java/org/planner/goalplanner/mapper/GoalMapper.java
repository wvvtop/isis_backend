package org.planner.goalplanner.mapper;

import org.planner.goalplanner.domain.Goal;
import org.planner.goalplanner.domain.Milestone;
import org.planner.goalplanner.domain.Task;
import org.planner.goalplanner.dto.goal.GoalDto;
import org.planner.goalplanner.dto.MilestoneResponse;
import org.planner.goalplanner.dto.task.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GoalMapper {
    public GoalDto mappingToGoalDto(Goal goal) {
        if(goal == null)
            return null;

        GoalDto goalDto = new GoalDto();
        goalDto.setId(goal.getId());
        goalDto.setTitle(goal.getTitle());
        goalDto.setDescription(goal.getDescription());
        goalDto.setStartDate(goal.getStartDate());
        goalDto.setEndDate(goal.getEndDate());
        goalDto.setCreatedAt(goal.getCreatedAt());

        if (goal.getUser() != null) {
            goalDto.setUserId(goal.getUser().getId());
        }

        if (goal.getMilestones() != null) {
            goalDto.setMilestones(
                    goal.getMilestones().stream()
                            .map(this::toMilestoneResponse)
                            .collect(Collectors.toList())
            );
        }
        return goalDto;
    }

    public MilestoneResponse toMilestoneResponse(Milestone milestone) {
        if (milestone == null) return null;
        MilestoneResponse dto = new MilestoneResponse();
        dto.setId(milestone.getId());
        dto.setTitle(milestone.getTitle());
        dto.setDescription(milestone.getDescription());
        dto.setOrder(milestone.getOrder());
        dto.setCreatedAt(milestone.getCreatedAt());

        if (milestone.getTasks() != null) {
            dto.setTasks(milestone.getTasks().stream()
                    .map(this::toTaskResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private TaskResponse toTaskResponse(Task task) {
        if (task == null) return null;
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setType(task.getType());
        dto.setStatus(task.getStatus());
        dto.setPlannedDate(task.getPlannedDate());
        dto.setCompletedAt(task.getCompletedAt());
        return dto;
    }
}
