package com.quickpick.ureca.V2.ticket.controller;

import com.quickpick.ureca.V2.ticket.service.TicketServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/tickets")
public class TicketControllerV2 {

    private final TicketServiceV2 ticketService;

    @PostMapping("/{ticketId}/order")
    public ResponseEntity<String> orderTicket(@PathVariable Long ticketId, @RequestParam Long userId) {
        ticketService.orderTicket(ticketId, userId);
        return ResponseEntity.ok("티켓 예매 성공");
    }

    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<String> cancelTicket(@PathVariable Long ticketId, @RequestParam Long userId) {
        ticketService.cancelTicket(ticketId, userId);
        return ResponseEntity.ok("티켓 예매 취소 성공");
    }
}
