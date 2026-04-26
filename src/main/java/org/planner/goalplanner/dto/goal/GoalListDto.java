package org.planner.goalplanner.dto.goal;

import java.time.LocalDate;

public class GoalListDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int milestonesCount;
    private double progress;           // процент выполнения (0-100)
    private String status;             // ACTIVE / COMPLETED

    // Конструкторы
    public GoalListDto() {}

    public GoalListDto(Long id, String title, String description, LocalDate startDate,
                       LocalDate endDate, int milestonesCount, double progress, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.milestonesCount = milestonesCount;
        this.progress = progress;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getMilestonesCount() { return milestonesCount; }
    public void setMilestonesCount(int milestonesCount) { this.milestonesCount = milestonesCount; }

    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
