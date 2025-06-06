package com.quickpick.ureca.v3.init;

import com.quickpick.ureca.v3.ticket.domain.TicketV3;
import com.quickpick.ureca.v3.ticket.repository.RedisStockRepositoryV3;
import com.quickpick.ureca.v3.ticket.repository.TicketRepositoryV3;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v3/init")
@RequiredArgsConstructor
public class InitControllerV3 {

    private final TicketRepositoryV3 ticketRepositoryV3;
    private final RedisStockRepositoryV3 redisStockRepositoryV3;
    private final JdbcTemplate jdbcTemplate;

    @PostMapping
    @Transactional
    public void init() {

        String sql = "INSERT INTO user (user_id, name, age, gender, password) VALUES (?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (long i = 1; i <= 10000; i++) {
            batchArgs.add(new Object[]{
                    i,
                    "abc" + i,
                    i+20,
                    "MALE",
                    "password" + i

            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
        System.out.println("=== 10만명 유저 생성 완료 ===");


        TicketV3 ticketV3 = TicketV3.builder()
                .name("ticket")
                .quantity(3000L)
                .reserveTime(LocalDateTime.now())
                .startDate(LocalDate.now())
                .build();

        TicketV3 saveTicketV3 = ticketRepositoryV3.save(ticketV3);
        redisStockRepositoryV3.setTicket(saveTicketV3.getId(), 3000L);
    }
}
