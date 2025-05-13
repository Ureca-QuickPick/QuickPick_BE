package com.quickpick.ureca.v1.common.init;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/init")
public class InitController {

    private final InitService initService;

    @PostMapping
    public String initializePost(
            @RequestParam(defaultValue = "1000") int ticketCount,
            @RequestParam(defaultValue = "10000") int userCount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reserveDate
    ) {
        return initService.initialize(ticketCount, userCount, startDate, reserveDate);
    }
}
