package com.quickpick.ureca.V2.ticket.service;

public interface TicketServiceV2 {
    void orderTicket(Long ticketId, Long userId);
    void cancelTicket(Long ticketId, Long userId);
}
