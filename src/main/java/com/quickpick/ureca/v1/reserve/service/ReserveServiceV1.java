package com.quickpick.ureca.v1.reserve.service;

import com.quickpick.ureca.v1.ticket.cache.TicketSoldOutCacheV1;
import com.quickpick.ureca.v1.ticket.domain.TicketV1;
import com.quickpick.ureca.v1.ticket.repository.TicketRepositoryV1;
import com.quickpick.ureca.v1.user.domain.User;
import com.quickpick.ureca.v1.user.repository.UserRepositoryV1;
import com.quickpick.ureca.v1.userticket.domain.UserTicketV1;
import com.quickpick.ureca.v1.userticket.repository.UserTicketRepositoryV1;
import com.quickpick.ureca.v1.userticket.repository.UserTicketShardingRepositoryV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReserveServiceV1 {

    @Autowired
    private TicketRepositoryV1 ticketRepositoryV1;

    @Autowired
    private UserRepositoryV1 userRepositoryV1;

    @Autowired
    private UserTicketRepositoryV1 userTicketRepositoryV1;

    @Autowired
    private UserTicketShardingRepositoryV1 userTicketShardingRepositoryV1;

    @Autowired
    private TicketSoldOutCacheV1 ticketSoldOutCacheV1;

    // 1.
//    // 티켓 예약 메서드 (락 X) Average : 586, Throughput : 17.5/sec
//    @Transactional
//    public void reserveTicket(Long userId, Long ticketId) {
//        // 티켓과 사용자 가져오기
//        TicketV1 ticket = ticketRepositoryV1.findById(ticketId).orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));
//        UserV1 user = userRepositoryV1.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        // 티켓 재고가 없으면 예외 발생
//        if (ticket.getQuantity() <= 0) {
//            throw new RuntimeException("매진되었습니다.");
//        }
//
//        // 티켓 재고 감소
//        System.out.println("감소!");
//        ticket.decreaseCount();
//        ticketRepositoryV1.save(ticket);  // 재고 감소 후 저장
//
//        // 예약 정보 저장
//        UserTicketV1 userTicket = new UserTicketV1(user, ticket);
//        userTicketRepositoryV1.save(userTicket);
//    }


    // 2.
    // 티켓 예약 메서드 (비관적 락) - Pessimistic Lock Average : 6691, Throughput : 419.9/sec
//    @Transactional
//    public void reserveTicket(Long userId, Long ticketId) {
//
//        log.info(">>> reserveTicket called: userId = {}, ticketId = {}", userId, ticketId);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        System.out.println("Reserve Ticket");
//        Ticket ticket = ticketRepositoryV1.findByIdForUpdate(ticketId)
//                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
//
//        if (ticket.getQuantity() <= 0) {
//            throw new IllegalStateException("Ticket out of stock");
//        }
//
//        ticket.setQuantity(ticket.getQuantity() - 1);
//        userTicketRepository.save(new UserTicket(user, ticket));
//    }

    // 3.
    // 티켓 예약 메서드 (비관적 락 + open-in-view) True Average : 14836, Throughput : 263.9/sec
    // 티켓 예약 메서드 (비관적 락 + open-in-view) False Average : 13589, Throughput : 275.8/sec

    // 티켓 예약 메서드 (비관적 락 + open-in-view + 네이티브 쿼리) True Average : 15919, Throughput : 244.7/sec
    // 티켓 예약 메서드 (비관적 락 + open-in-view + 네이티브 쿼리) False Average : 14961, Throughput : 262.3/sec

    // 티켓 예약 메서드 (open-in-view + FetchJoin + DTO) False Average : 69005, Throughput : 64.9/sec

//    @Transactional
//    public Ticket reserveTicket(Long userId, Long ticketId) {
//
//        log.info(">>> reserveTicket called: userId = {}, ticketId = {}", userId, ticketId);
//
//        // user는 fetch join 하지 않았으므로 별도로 조회
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        // fetch join으로 모든 필요한 정보 로딩
//        Ticket ticket = ticketRepositoryV1.findByIdForUpdateNative(ticketId);
//
//        if (userTicketRepository.existsByUser_UserIdAndTicket_TicketId(userId, ticketId)) {
//            log.warn("이미 예약한 유저입니다. userId={}, ticketId={}", userId, ticketId);
//            return ticket; // 중복이면 insert 안 하고 그냥 리턴
//        }
//
//        if (ticket.getQuantity() <= 0) {
//            throw new IllegalStateException("Ticket out of stock");
//        }
//
//        ticket.setQuantity(ticket.getQuantity() - 1);
//        userTicketRepository.save(new UserTicket(user, ticket));
//        return ticket;
//    }


    // 4.
    // 티켓 예약 메서드 (비관적 락 + 중복방지 + 인덱스 + 네이티브 쿼리) Average : 9734, Throughput : 339.2/sec
//    @Transactional
//    public void reserveTicket(Long userId, Long ticketId) {
//
//        log.info(">>> reserveTicket called: userId = {}, ticketId = {}", userId, ticketId);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        System.out.println("Reserve Ticket");
//        Ticket ticket = ticketRepositoryV1.findByIdForUpdateNative(ticketId);
//
//        if (userTicketRepository.existsUserTicketRaw(userId, ticketId) != null) {
//            log.warn("이미 예약한 유저입니다. userId={}, ticketId={}", userId, ticketId);
//            return;
//        }
//
//        // 이 부분이 Redis를 사용하면 더 효율적으로 변경 가능
//        if (ticket.getQuantity() <= 0) {
//            throw new IllegalStateException("Ticket out of stock");
//        }
//
//        ticket.setQuantity(ticket.getQuantity() - 1);
//        userTicketRepository.save(new UserTicket(user, ticket));
//    }

    // 5.
    // 티켓 예약 메서드 (비관적 락 + 중복방지 + open-in-view(True) + 인덱스 + Projection + 네이티브 쿼리) Average : 12094, Throughput : 339.1/sec
    // 티켓 예약 메서드 (비관적 락 + 중복방지 + open-in-view(False) + Projection + 네이티브 쿼리) Average : 13033, Throughput : 293.7/sec
//    @Transactional
//    public void reserveTicket(Long userId, Long ticketId) {
//
//        log.info(">>> reserveTicket called: userId = {}, ticketId = {}", userId, ticketId);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        // quantity만 조회하는 Projection으로 변경
//        TicketQuantityProjection ticketProjection = ticketRepositoryV1.findQuantityForUpdate(ticketId);
//        if (ticketProjection == null) {
//            throw new IllegalArgumentException("Ticket not found");
//        }
//
//        if (userTicketRepository.existsUserTicketRaw(userId, ticketId) != null) {
//            log.warn("이미 예약한 유저입니다. userId={}, ticketId={}", userId, ticketId);
//            return;
//        }
//
//        if (ticketProjection.getQuantity() <= 0) {
//            throw new IllegalStateException("Ticket out of stock");
//        }
//
//        // 수량 감소는 직접 쿼리로 처리하거나, 엔티티 조회 후 업데이트 필요
//        ticketRepositoryV1.decreaseQuantity(ticketId); // 이 메서드는 아래에 작성
//        userTicketRepository.save(new UserTicket(user, ticketRepositoryV1.getReferenceById(ticketId)));
//
//    }


    // 티켓 예약 메서드 (비관적 락 + 중복방지 + In-Memory 캐시 + Sharding + 네이티브 쿼리) Average : 13016, Throughput : 474.7/sec
    @Transactional
    public void reserveTicket(Long userId, Long ticketId) {
        log.info("Reserving ticket: userId = {}, ticketId = {}", userId, ticketId);

        if (ticketSoldOutCacheV1.isSoldOut(ticketId)) {
            throw new IllegalStateException("이미 매진된 티켓입니다.");
        }

        User user = userRepositoryV1.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userTicketShardingRepositoryV1.exists(userId, ticketId)) {
            throw new IllegalStateException("이미 예약함");
        }

        TicketV1 ticket = ticketRepositoryV1.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (ticket.getQuantity() <= 0) {
            ticketSoldOutCacheV1.markSoldOut(ticketId); // 캐시 반영
            throw new IllegalStateException("재고 없음");
        }

        ticket.setQuantity(ticket.getQuantity() - 1);

        userTicketShardingRepositoryV1.saveIgnoreDuplicate(
                new UserTicketV1(user, ticketRepositoryV1.getReferenceById(ticketId))
        );
    }

    // 예약 취소 메서드
    @Transactional
    public void cancelReservation(Long userId, Long ticketId) {
        log.info("Cancelling reservation: userId = {}, ticketId = {}", userId, ticketId);

        // 예약 존재 여부 확인
        if (!userTicketShardingRepositoryV1.exists(userId, ticketId)) {
            throw new IllegalStateException("예약 내역이 존재하지 않습니다.");
        }

        // 예약 삭제
        userTicketShardingRepositoryV1.delete(userId, ticketId);

        // 티켓 수량 복원 (비관적 락으로 안전하게 처리)
        TicketV1 ticket = ticketRepositoryV1.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setQuantity(ticket.getQuantity() + 1);

        // 매진 캐시 초기화 (optional)
        if (ticket.getQuantity() > 0) {
            ticketSoldOutCacheV1.unmarkSoldOut(ticketId);
        }
    }


}