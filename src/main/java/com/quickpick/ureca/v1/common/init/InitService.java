package com.quickpick.ureca.v1.common.init;

import com.quickpick.ureca.v1.ticket.domain.TicketV1;
import com.quickpick.ureca.v1.ticket.repository.TicketRepositoryV1;
import com.quickpick.ureca.v1.user.domain.User;
import com.quickpick.ureca.v1.user.repository.UserRepositoryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InitService {

    private final TicketRepositoryV1 ticketRepository;
    private final UserRepositoryV1 userRepositoryV1;

    public String initialize(int ticketCount, int userCount, LocalDateTime startDate, LocalDateTime reserveDate) {
        ticketRepository.deleteAll();
        userRepositoryV1.deleteAll();

        TicketV1 ticket = TicketV1.builder()
                .name("테스트 티켓")
                .quantity(ticketCount)
                .startDate(startDate != null ? startDate : LocalDateTime.now().plusDays(1))
                .reserveDate(reserveDate != null ? reserveDate : LocalDateTime.now())
                .build();
        ticketRepository.save(ticket);

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            User user = User.builder()
                    .id("user" + i)
                    .password("pw" + i)
                    .name("User" + i)
                    .age("20")
                    .gender("M")
                    .build();
            users.add(user);
        }
        userRepositoryV1.saveAll(users);

        return "초기화 완료: 티켓 1개(" + ticketCount + "개 수량), 유저 " + userCount + "명 생성";
    }
}
