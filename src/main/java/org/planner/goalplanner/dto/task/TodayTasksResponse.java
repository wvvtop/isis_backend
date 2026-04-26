package org.planner.goalplanner.dto.task;

import org.planner.goalplanner.dto.task.TaskDto;

import java.util.List;

public class TodayTasksResponse {

    private List<TaskDto> main;
    private List<TaskDto> side;

    // Конструкторы
    public TodayTasksResponse() {}

    public TodayTasksResponse(List<TaskDto> main, List<TaskDto> side) {
        this.main = main;
        this.side = side;
    }

    // Геттеры и сеттеры
    public List<TaskDto> getMain() { return main; }
    public void setMain(List<TaskDto> main) { this.main = main; }

    public List<TaskDto> getSide() { return side; }
    public void setSide(List<TaskDto> side) { this.side = side; }
}
