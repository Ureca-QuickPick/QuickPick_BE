package com.quickpick.ureca.v1.ticket.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TicketSoldOutCacheV1 {
    private final ConcurrentHashMap<Long, Boolean> soldOutMap = new ConcurrentHashMap<>();

    public boolean isSoldOut(Long ticketId) {
        return soldOutMap.getOrDefault(ticketId, false);
    }

    public void markSoldOut(Long ticketId) {
        soldOutMap.put(ticketId, true);
    }

    public void unmarkSoldOut(Long ticketId) {
        soldOutMap.remove(ticketId);
    }
}
