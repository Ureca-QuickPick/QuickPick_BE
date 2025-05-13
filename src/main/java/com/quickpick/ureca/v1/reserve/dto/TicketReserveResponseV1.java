package com.quickpick.ureca.v1.reserve.dto;

import com.quickpick.ureca.v1.ticket.domain.TicketV1;
import com.quickpick.ureca.v1.user.domain.User;

public record TicketReserveResponseV1(
        Long ticketId,
        String ticketName,
        int remainingQuantity,
        String reservedByUsername
) {
    public static TicketReserveResponseV1 of(TicketV1 ticket, User user) {
        return new TicketReserveResponseV1(
                ticket.getTicketId(),
                ticket.getName(),
                ticket.getQuantity(),
                user.getId()
        );
    }
}
