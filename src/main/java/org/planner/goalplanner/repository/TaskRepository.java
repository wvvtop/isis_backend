package org.planner.goalplanner.repository;

import jakarta.persistence.QueryHint;
import org.planner.goalplanner.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdAndPlannedDate(Long userId, LocalDate plannedDate);

    @Query(value = "SELECT * FROM task WHERE user_id = :userId AND goal_id = :goalId AND deleted = true",
            nativeQuery = true)
    List<Task> findDeletedByUserIdAndGoalId(@Param("userId") Long userId,
                                            @Param("goalId") Long goalId);

    @Query(value = "SELECT * FROM task WHERE id = :id", nativeQuery = true)
    Optional<Task> findByIdIncludingDeletedNative(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE task SET deleted = false WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);
}

