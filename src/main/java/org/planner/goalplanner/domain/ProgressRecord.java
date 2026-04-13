package org.planner.goalplanner.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.planner.goalplanner.enums.PeriodType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_record")
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "completion_rate", nullable = false)
    private Double completionRate; // или BigDecimal

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // конструкторы, геттеры, сеттеры
    public ProgressRecord() {}
}
