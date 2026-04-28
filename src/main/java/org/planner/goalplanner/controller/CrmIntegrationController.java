package org.planner.goalplanner.controller;


import org.planner.goalplanner.domain.User;
import org.planner.goalplanner.dto.crm.InactiveUserDto;
import org.planner.goalplanner.dto.crm.TicketDto;
import org.planner.goalplanner.dto.crm.TicketRequestDto;
import org.planner.goalplanner.dto.crm.TicketResponseDto;
import org.planner.goalplanner.repository.UserRepository;
import org.planner.goalplanner.service.CrmService;
import org.planner.goalplanner.service.UserActivityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CrmIntegrationController {
    private final CrmService crmService;
    private final UserActivityService userActivityService;

    @Value("${crm.api.url:}")
    private String crmApiUrl;

    @Value("${crm.api.key:}")
    private String crmApiKey;

    public CrmIntegrationController(UserActivityService userActivityService, CrmService crmService) {
        this.userActivityService = userActivityService;
        this.crmService = crmService;
    }

    @GetMapping("/crm/inactive-users")
    public ResponseEntity<List<InactiveUserDto>> getInactiveUsers(
            @RequestParam(defaultValue = "7") int days,
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {

        // Проверка API ключа
        if (crmApiKey != null && !crmApiKey.isEmpty() && !crmApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<InactiveUserDto> result = userActivityService.getInactiveUsers(days);
        return ResponseEntity.ok(result);
    }

    // ========== СЦЕНАРИЙ 2: Создание тикета (фронт → бэк → CRM) ==========

    @PostMapping("/tickets/create")
    public ResponseEntity<TicketResponseDto> createTicket(@RequestBody TicketRequestDto ticketRequest,
                                                          Authentication authentication) {
        String userEmail = authentication.getName();
        // Проверка обязательных полей
        if (userEmail == null || ticketRequest.getSubject() == null) {
            return ResponseEntity.badRequest().body(
                    new TicketResponseDto("error", "Email and subject are required", null)
            );
        }
        TicketResponseDto response = crmService.createTicket(
                userEmail,
                ticketRequest.getSubject(),
                ticketRequest.getMessage()
        );

        HttpStatus status = response.getStatus().equals("error")
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : HttpStatus.OK;

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> getTickets(Authentication authentication) {
        String userEmail = authentication.getName();

        List<TicketDto> tickets = crmService.getTickets(userEmail);

        return ResponseEntity.ok(tickets);
    }
}
