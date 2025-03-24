package site.easy.to.build.crm.dto;

import java.time.LocalDateTime;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Ticket;

public class TicketDto {

    public int ticketId;
    public String subject;
    public String status;
    public String priority;
    public LocalDateTime createdAt;
    public Depense depense;

    public TicketDto(Ticket ticket){
        this.ticketId = ticket.getTicketId();
        this.subject = ticket.getSubject();
        this.status = ticket.getStatus();
        this.priority = ticket.getPriority();
        this.createdAt = ticket.getCreatedAt();
        this.depense = ticket.getDepense();
    }
}
