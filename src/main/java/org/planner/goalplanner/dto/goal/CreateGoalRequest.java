package org.planner.goalplanner.dto.goal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.planner.goalplanner.dto.CreateMilestoneRequest;

import java.time.LocalDate;
import java.util.List;

public class CreateGoalRequest {

    @NotBlank(message = "Название цели обязательно")
    private String title;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private List<CreateMilestoneRequest> milestones;

    // Конструкторы
    public CreateGoalRequest() {}

    public CreateGoalRequest(String title, String description, LocalDate startDate,
                             LocalDate endDate, List<CreateMilestoneRequest> milestones) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.milestones = milestones;
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

    public List<CreateMilestoneRequest> getMilestones() { return milestones; }
    public void setMilestones(List<CreateMilestoneRequest> milestones) { this.milestones = milestones; }
}
