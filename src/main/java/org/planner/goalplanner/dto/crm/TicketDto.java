package org.planner.goalplanner.dto.crm;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class TicketDto {
    private String subject;
    private String message;
    private String status;
    private String priority;
    private String managerResponse;

    public TicketDto(String subject, String message, String status, String priority, String managerResponse) {
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.priority = priority;
        this.managerResponse = managerResponse;
    }

    // getters

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getManagerResponse() {
        return managerResponse;
    }

    public void setManagerResponse(String managerResponse) {
        this.managerResponse = managerResponse;
    }
}