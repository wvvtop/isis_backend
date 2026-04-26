package org.planner.goalplanner.dto.goal;

import org.planner.goalplanner.dto.MilestoneResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoalDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private Long userId;               // только идентификатор пользователя
    private List<MilestoneResponse> milestones; // упрощённые вехи

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<MilestoneResponse> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneResponse> milestones) {
        this.milestones = milestones;
    }

    // конструкторы, геттеры, сеттеры (или Lombok @Data)
}
