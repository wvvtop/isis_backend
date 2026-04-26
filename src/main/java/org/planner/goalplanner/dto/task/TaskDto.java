package org.planner.goalplanner.dto.task;

import org.planner.goalplanner.enums.TaskStatus;
import org.planner.goalplanner.enums.TaskType;

import java.time.LocalDate;

public class TaskDto {

    private Long id;
    private String title;
    private TaskType type;
    private TaskStatus status;
    private LocalDate plannedDate;

    // Конструкторы
    public TaskDto() {}

    public TaskDto(Long id, String title, TaskType type, TaskStatus status, LocalDate plannedDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.plannedDate = plannedDate;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TaskType getType() { return type; }
    public void setType(TaskType type) { this.type = type; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDate getPlannedDate() { return plannedDate; }
    public void setPlannedDate(LocalDate plannedDate) { this.plannedDate = plannedDate; }
}
