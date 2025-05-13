package com.quickpick.ureca.v1.reserve.controller;

import com.quickpick.ureca.v1.reserve.service.ReserveServiceV1;
import com.quickpick.ureca.v1.user.repository.UserRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-reserve")
@Slf4j
public class ReserveControllerV1 {

    private final ReserveServiceV1 reserveServiceV1;
    private final UserRepositoryV1 userRepositoryV1;

    public ReserveControllerV1(ReserveServiceV1 reserveServiceV1, UserRepositoryV1 userRepositoryV1) {
        this.reserveServiceV1 = reserveServiceV1;
        this.userRepositoryV1 = userRepositoryV1;
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserve(@RequestParam Long userId, @RequestParam Long ticketId) {
        try {
            reserveServiceV1.reserveTicket(userId, ticketId);
            return ResponseEntity.ok("예약 성공");
        } catch (Exception e) {
            log.error("예약 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예약 실패: " + e.getMessage());
        }
    }

    // open-in-view + Fetch-Join + DTO
//    @PostMapping
//    public ResponseEntity<TicketReserveResponse> reserve(@RequestParam Long userId, @RequestParam Long ticketId) {
//        try {
//            Ticket ticket = reserveServiceV1.reserveTicket(userId, ticketId);
//            User user = userRepository.findById(userId).orElseThrow();
//            return ResponseEntity.ok(TicketReserveResponse.of(ticket, user));
//        } catch (Exception e) {
//            log.error("예약 실패: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestParam Long userId, @RequestParam Long ticketId) {
        log.info("{}의 티켓 취소 요청", userId);
        reserveServiceV1.cancelReservation(userId, ticketId);
        return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
    }
}
