package org.planner.goalplanner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Entity
@Table(name="usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email(message = "Введите корректный email!")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer max_main_tasks_per_day = 3;

    @Column(nullable = false)
    private Integer max_side_tasks_per_day = 10;

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMax_main_tasks_per_day() {
        return max_main_tasks_per_day;
    }


    public void setMax_main_tasks_per_day(Integer max_main_tasks_per_day) {
        this.max_main_tasks_per_day = max_main_tasks_per_day;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getMax_side_tasks_per_day() {
        return max_side_tasks_per_day;
    }

    public void setMax_side_tasks_per_day(Integer max_side_tasks_per_day) {
        this.max_side_tasks_per_day = max_side_tasks_per_day;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
