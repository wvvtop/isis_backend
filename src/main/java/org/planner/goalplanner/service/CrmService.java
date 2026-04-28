package org.planner.goalplanner.service;

import org.planner.goalplanner.dto.crm.CreateTicketRequest;
import org.planner.goalplanner.dto.crm.TicketDto;
import org.planner.goalplanner.dto.crm.TicketResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CrmService {
    private final RestTemplate restTemplate;

    @Value("${crm.api.url:}")
    private String crmApiUrl;

    @Value("${crm.api.key:}")
    private String crmApiKey;

    public CrmService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Проверка, настроена ли CRM интеграция
     */
    public boolean isCrmConfigured() {
        return crmApiUrl != null && !crmApiUrl.isEmpty();
    }

    /**
     * Создание тикета в CRM
     */
    public TicketResponseDto createTicket(String userEmail, String subject, String message) {
        // Если CRM не настроена
        if (!isCrmConfigured()) {
            return new TicketResponseDto(
                    "local_only",
                    "Ticket saved locally (CRM not configured)",
                    null
            );
        }

        // Отправляем в CRM
        try {
            HttpHeaders headers = createHttpHeaders();
            CreateTicketRequest crmRequest = new CreateTicketRequest(
                    userEmail,
                    userEmail,
                    subject,
                    message != null ? message : "",
                    "new"
            );

            HttpEntity<CreateTicketRequest> entity = new HttpEntity<>(crmRequest, headers);
            String crmEndpoint = crmApiUrl + "/CTicket";

            ResponseEntity<String> crmResponse = restTemplate.postForEntity(
                    crmEndpoint,
                    entity,
                    String.class
            );

            return new TicketResponseDto(
                    "success",
                    "Ticket created in CRM",
                    crmResponse.getBody()
            );

        } catch (Exception e) {
            return new TicketResponseDto(
                    "error",
                    "Failed to create ticket in CRM: " + e.getMessage(),
                    null
            );
        }
    }

    public List<TicketDto> getTickets(String userEmail) {
        if (!isCrmConfigured()) {
            return Collections.emptyList();
        }

        try {
            HttpHeaders headers = createHttpHeaders();

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder
                    .fromUriString(crmApiUrl + "/CTicket")
                    .queryParam("where[0][attribute]", "email")
                    .queryParam("where[0][type]", "equals")
                    .queryParam("where[0][value]", userEmail)
                    .queryParam("orderBy", "createdAt")
                    .queryParam("order", "desc")
                    .toUriString();
            URI uri = URI.create(url);
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            System.out.println(response);
            List<Map<String, Object>> list = (List<Map<String, Object>>) response.getBody().get("list");
            System.out.println(list);
            return list.stream().map(item -> new TicketDto(
                    (String) item.get("subject"),
                    (String) item.get("message"),
                    (String) item.get("status"),
                    (String) item.get("priority"),
                    (String) item.get("managerresponse")
            )).toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Создание HTTP заголовков для CRM запросов
     */
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (crmApiKey != null && !crmApiKey.isEmpty()) {
            headers.set("X-API-Key", crmApiKey);
        }

        return headers;
    }
}
