package org.planner.goalplanner.service;

import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.crm.InactiveUserDto;
import org.planner.goalplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivityService {
    private final UserRepository userRepository;

    public UserActivityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получение неактивных пользователей
     */
    public List<InactiveUserDto> getInactiveUsers(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        List<User> inactiveUsers = userRepository.findByLastActiveAtBefore(cutoff);

        return inactiveUsers.stream()
                .map(user -> {
                    long daysInactive = ChronoUnit.DAYS.between(user.getLastActiveAt(), LocalDateTime.now());
                    return new InactiveUserDto(
                            user.getEmail(),
                            (int) daysInactive,
                            user.getEmail()
                    );
                })
                .collect(Collectors.toList());
    }
}
