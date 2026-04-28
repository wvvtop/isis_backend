package org.planner.goalplanner.dto.crm;

public class TicketResponseDto {
    private String status;
    private String message;
    private String crmTicketId;

    public TicketResponseDto() {}

    public TicketResponseDto(String status, String message, String crmTicketId) {
        this.status = status;
        this.message = message;
        this.crmTicketId = crmTicketId;
    }

    // Геттеры и сеттеры
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCrmTicketId() { return crmTicketId; }
    public void setCrmTicketId(String crmTicketId) { this.crmTicketId = crmTicketId; }
}
