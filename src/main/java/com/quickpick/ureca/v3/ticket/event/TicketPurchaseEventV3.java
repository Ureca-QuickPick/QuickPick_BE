package com.quickpick.ureca.v3.ticket.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketPurchaseEventV3 {
    private String uuid;
    private Long ticketId;
    private Long userId;
    private Long quantity;
    private String time;
}