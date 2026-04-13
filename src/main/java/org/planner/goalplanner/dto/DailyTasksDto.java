package org.planner.goalplanner.dto;

import java.util.ArrayList;
import java.util.List;

public class DailyTasksDto {

    private List<String> main = new ArrayList<>();
    private List<String> side = new ArrayList<>();

    // Конструкторы
    public DailyTasksDto() {}

    public DailyTasksDto(List<String> main, List<String> side) {
        this.main = main != null ? main : new ArrayList<>();
        this.side = side != null ? side : new ArrayList<>();
    }

    // Геттеры и сеттеры
    public List<String> getMain() { return main; }
    public void setMain(List<String> main) {
        this.main = main != null ? main : new ArrayList<>();
    }

    public List<String> getSide() { return side; }
    public void setSide(List<String> side) {
        this.side = side != null ? side : new ArrayList<>();
    }
}
