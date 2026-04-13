package org.planner.goalplanner.repository;

import org.planner.goalplanner.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdAndPlannedDate(Long userId, LocalDate plannedDate);
}
