package org.planner.goalplanner.repository;

import org.planner.goalplanner.domain.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    Optional<Milestone> findById(Long id);
}
