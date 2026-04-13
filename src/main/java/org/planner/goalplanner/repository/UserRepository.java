package org.planner.goalplanner.repository;

import org.planner.goalplanner.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Очень нужный метод

    boolean existsByEmail(String email);
}
