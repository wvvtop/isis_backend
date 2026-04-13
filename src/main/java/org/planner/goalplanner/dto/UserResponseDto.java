package org.planner.goalplanner.dto;

import org.planner.goalplanner.domain.User;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long id;
    private String email;
    private Integer maxMainTasksPerDay;
    private LocalDateTime createdAt;

    // Конструктор из User
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.maxMainTasksPerDay = user.getMax_main_tasks_per_day();
        this.createdAt = user.getCreated_at();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMaxMainTasksPerDay() {
        return maxMainTasksPerDay;
    }

    public void setMaxMainTasksPerDay(Integer maxMainTasksPerDay) {
        this.maxMainTasksPerDay = maxMainTasksPerDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
