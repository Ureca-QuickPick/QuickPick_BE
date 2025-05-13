package com.quickpick.ureca.v3.ticket.controller;

import com.quickpick.ureca.v3.ticket.service.TicketServiceV3;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v3/tickets")
@RequiredArgsConstructor
@RestController
public class TicketControllerV3 {

    private final TicketServiceV3 ticketServiceV3;

    @PostMapping("/{ticketId}/purchase")
    public String purchaseTicket(@PathVariable Long ticketId, @RequestParam Long userId) {
        ticketServiceV3.purchaseTicket(ticketId, userId, 1L);
        return "success";
    }
}
