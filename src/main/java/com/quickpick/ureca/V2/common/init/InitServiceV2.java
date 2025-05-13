//package com.quickpick.ureca.V2.common.init;
//
//import com.quickpick.ureca.V2.ticket.domain.TicketV2;
//import com.quickpick.ureca.V2.ticket.repository.TicketRepositoryV2;
//import com.quickpick.ureca.V2.user.domain.UserV2;
//import com.quickpick.ureca.V2.user.repository.UserRepositoryV2;
//import lombok.RequiredArgsConstructor;
//import org.redisson.api.RedissonClient;
//import org.redisson.client.codec.StringCodec;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InitServiceV2 {
//
//    private final TicketRepositoryV2 ticketRepository;
//    private final UserRepositoryV2 userRepository;
//    private final RedissonClient redissonClient;
//
//    public String initialize(int ticketCount, int userCount, LocalDateTime startDate, LocalDateTime reserveDate) {
//        // DB 초기화
//        ticketRepository.deleteAll();
//        userRepository.deleteAll();
//
//        // 티켓 생성 및 저장 (ID 생성)
//        TicketV2 ticket = TicketV2.builder()
//                .name("테스트 티켓")
//                .quantity(ticketCount)
//                .startDate(startDate != null ? startDate : LocalDateTime.now().plusDays(1))
//                .reserveDate(reserveDate != null ? reserveDate : LocalDateTime.now())
//                .build();
//        ticket = ticketRepository.save(ticket); // ID 보장
//
//        // Redis 키 초기화
//        String redisStockKey = "ticket:stock:" + ticket.getTicketId();
//        String redisUserSetKey = "ticket:users:" + ticket.getTicketId();
//
//        // 재고 수량 설정
//        redissonClient.getBucket(redisStockKey, StringCodec.INSTANCE).set(String.valueOf(ticketCount));
//        // 중복 예매 유저 Set 초기화
//        redissonClient.getSet(redisUserSetKey, StringCodec.INSTANCE).delete();
//
//        // 유저 생성
//        List<UserV2> users = new ArrayList<>();
//        for (int i = 1; i <= userCount; i++) {
//            UserV2 user = UserV2.builder()
//                    .id("user" + i)
//                    .password("pw" + i)
//                    .name("User" + i)
//                    .age("20")
//                    .gender("M")
//                    .build();
//            users.add(user);
//        }
//        userRepository.saveAll(users);
//
//        return "초기화 완료: 티켓 1개(" + ticketCount + "개 수량), 유저 " + userCount + "명 생성";
//    }
//}