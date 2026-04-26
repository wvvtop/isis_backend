package org.planner.goalplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.planner.goalplanner.dto.goal.DailyTasksDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CreateMilestoneRequest {

    @NotBlank(message = "Название этапа обязательно")
    private String title;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Map<String, DailyTasksDto> tasks = new HashMap<>();

    // Конструкторы
    public CreateMilestoneRequest() {}

    public CreateMilestoneRequest(String title, String description, LocalDate startDate,
                                  LocalDate endDate, Map<String, DailyTasksDto> tasks) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = tasks != null ? tasks : new HashMap<>();
    }

    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Map<String, DailyTasksDto> getTasks() { return tasks; }
    public void setTasks(Map<String, DailyTasksDto> tasks) {
        this.tasks = tasks != null ? tasks : new HashMap<>();
    }
}
