package org.planner.goalplanner.dto.crm;

public class InactiveUserDto {
    private String email;

    private String name;
    private int daysInactive;

    public InactiveUserDto() {}

    public InactiveUserDto(String email, int daysInactive, String name) {
        this.email = email;
        this.daysInactive = daysInactive;
        this.name = name;
    }

    // Геттеры
    public String getEmail() { return email; }
    public int getDaysInactive() { return daysInactive; }

    // Сеттеры
    public void setEmail(String email) { this.email = email; }
    public void setDaysInactive(int daysInactive) { this.daysInactive = daysInactive; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
