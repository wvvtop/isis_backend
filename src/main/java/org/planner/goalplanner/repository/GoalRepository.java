package org.planner.goalplanner.repository;

import org.planner.goalplanner.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // При необходимости можно добавить кастомные методы, например:
     List<Goal> findByUserId(Long userId);
}
