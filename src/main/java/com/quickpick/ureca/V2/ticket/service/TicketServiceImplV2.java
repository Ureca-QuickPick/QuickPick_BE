package com.quickpick.ureca.V2.ticket.service;

import com.quickpick.ureca.V2.ticket.domain.TicketV2;
import com.quickpick.ureca.V2.ticket.repository.TicketRepositoryV2;
import com.quickpick.ureca.V2.user.domain.UserV2;
import com.quickpick.ureca.V2.user.repository.UserRepositoryV2;
import com.quickpick.ureca.V2.userticket.domain.UserTicketV2;
import com.quickpick.ureca.V2.userticket.repository.UserTicketRepositoryV2;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImplV2 implements TicketServiceV2 {
    private final TicketRepositoryV2 ticketRepository;
    private final UserRepositoryV2 userRepository;
    private final UserTicketRepositoryV2 userTicketRepository;
    private final RedissonClient redissonClient;

    // Lua 스크립트 및 SHA1 캐싱용 변수
    private String reserveLuaSha;
    private String rollbackLuaSha;
    private final Map<Long, TicketV2> ticketCache = new ConcurrentHashMap<>();

    /**
     * TEST 2
     */
//    @Override
//    @Transactional
//    public void orderTicket(Long ticketId, Long userId) {
//        int retry = 3; // 재시도 횟수
//        while (retry-- > 0) {
//            try {
//                processOrder(ticketId, userId);
//                return; // 성공하면 종료
//            } catch (ObjectOptimisticLockingFailureException e) {
//                if (retry == 0) throw e;
//            }
//        }
//    }
//
//    private void processOrder(Long ticketId, Long userId) {
//        TicketV2 ticket = ticketRepository.findById(ticketId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
//
//        if (ticket.getQuantity() <= 0) {
//            throw new RuntimeException("매진된 티켓입니다.");
//        }
//
//        UserV2 user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//
//        if (userTicketRepository.existsByUserAndTicket(user, ticket)) {
//            throw new RuntimeException("이미 예매한 티켓입니다.");
//        }
//
//        ticket.setQuantity(ticket.getQuantity() - 1);
//        ticketRepository.save(ticket); // save() 시 OptimisticLock 체크 발생
//
//        UserTicketV2 userTicket = new UserTicketV2(user, ticket);
//        userTicketRepository.save(userTicket);
//    }


    /**
     * TEST3
     */
//    @Override
//    @Transactional
//    public void orderTicket(Long ticketId, Long userId) {
//        RLock lock = redissonClient.getLock("ticketLock:" + ticketId);
//
//        boolean isLocked = false;
//        try {
//            // 최대 2초 대기, 5초 안에 락 자동 해제
//            isLocked = lock.tryLock(2, 5, TimeUnit.SECONDS);
//
//            if (!isLocked) {
//                throw new RuntimeException("잠시 후 다시 시도해주세요.");
//            }
//
//            Ticket ticket = ticketRepository.findById(ticketId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
//            if (ticket.getQuantity() <= 0) {
//                throw new RuntimeException("매진된 티켓입니다.");
//            }
//
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//            if (userTicketRepository.existsByUserAndTicket(user, ticket)) {
//                throw new RuntimeException("이미 예매한 티켓입니다.");
//            }
//
//            ticket.setQuantity(ticket.getQuantity() - 1);
//            ticketRepository.save(ticket);
//
//            UserTicket userTicket = new UserTicket(user, ticket);
//            userTicketRepository.save(userTicket);
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException("락 획득 실패", e);
//        } finally {
//            if (isLocked) {
//                lock.unlock();
//            }
//        }
//    }

    /**
     * TEST4
     */
//    @Override
//    public void orderTicket(Long ticketId, Long userId) {
//        RLock lock = redissonClient.getLock("ticketLock:" + ticketId);
//        boolean isLocked = false;
//        Ticket ticket;
//
//        try {
//            // 최대 2초 대기 후 락 획득 시도, 락은 5초 후 자동 해제
//            isLocked = lock.tryLock(2, 5, TimeUnit.SECONDS);
//            if (!isLocked) {
//                throw new RuntimeException("잠시 후 다시 시도해주세요.");
//            }
//
//            // 락 안에서: 티켓 조회 및 재고 감소
//            ticket = ticketRepository.findById(ticketId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
//
//            if (ticket.getQuantity() <= 0) {
//                throw new RuntimeException("매진된 티켓입니다.");
//            }
//
//            ticket.setQuantity(ticket.getQuantity() - 1);
//            ticketRepository.save(ticket);
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException("락 획득 실패", e);
//        } finally {
//            if (isLocked && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//
//        // 락 외부: 유저 조회 및 예매 중복 검사, 예매 저장
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//
//        if (userTicketRepository.existsByUserAndTicket(user, ticket)) {
//            throw new RuntimeException("이미 예매한 티켓입니다.");
//        }
//
//        UserTicket userTicket = new UserTicket(user, ticket);
//        userTicketRepository.save(userTicket);
//    }


    /**
     * TEST5
     */
//    @Override
//    public void orderTicket(Long ticketId, Long userId) {
//        String stockKey = "ticket:stock:" + ticketId;
//
//        String luaScript =
//                "local stock = redis.call('GET', KEYS[1])\n" +
//                        "if not stock then return -1 end\n" +
//                        "stock = tonumber(stock)\n" +
//                        "if stock <= 0 then return -1 end\n" +
//                        "redis.call('DECR', KEYS[1])\n" +
//                        "return 1";
//
//        Long result;
//        try {
//            result = redissonClient.getScript().eval(
//                    RScript.Mode.READ_WRITE,
//                    luaScript,
//                    RScript.ReturnType.INTEGER,
//                    Collections.singletonList(stockKey)
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Lua 실행 실패: " + e.getMessage(), e);
//        }
//
//        if (result == null || result != 1L) {
//            throw new RuntimeException("매진된 티켓입니다.");
//        }
//
//        // 이후 DB에서 ticket, user 조회 및 중복 예매 확인 → UserTicket 저장
//        Ticket ticket = ticketRepository.findById(ticketId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//        if (userTicketRepository.existsByUserAndTicket(user, ticket)) {
//            throw new RuntimeException("이미 예매한 티켓입니다.");
//        }
//        UserTicket userTicket = new UserTicket(user, ticket);
//        userTicketRepository.save(userTicket);
//    }

    /**
     * TEST6
     */
//    @Override
//    @Transactional
//    public void orderTicket(Long ticketId, Long userId) {
//        String stockKey = "ticket:stock:" + ticketId;
//        String userSetKey = "ticket:users:" + ticketId;
//
//        String luaScript =
//                "local stock = redis.call('GET', KEYS[1])\n" +
//                        "if not stock then return -1 end\n" +
//                        "stock = tonumber(stock)\n" +
//                        "if stock <= 0 then return -1 end\n" +
//                        "local exists = redis.call('SISMEMBER', KEYS[2], ARGV[1])\n" +
//                        "if exists == 1 then return -2 end\n" +
//                        "redis.call('DECR', KEYS[1])\n" +
//                        "redis.call('SADD', KEYS[2], ARGV[1])\n" +
//                        "return 1";
//
//        Long result;
//        try {
//            result = redissonClient.getScript(StringCodec.INSTANCE).eval(
//                    RScript.Mode.READ_WRITE,
//                    luaScript,
//                    RScript.ReturnType.INTEGER,
//                    Arrays.asList(stockKey, userSetKey),
//                    userId.toString()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Lua 실행 실패: " + e.getMessage(), e);
//        }
//
//        if (result == -1L) {
//            throw new RuntimeException("매진된 티켓입니다.");
//        }
//        if (result == -2L) {
//            throw new RuntimeException("이미 예매한 유저입니다.");
//        }
//
//        try {
//            Ticket ticket = ticketRepository.findById(ticketId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//
//            UserTicket userTicket = new UserTicket(user, ticket);
//            userTicketRepository.save(userTicket);
//        } catch (Exception e) {
//            // Redis 재고 복구
//            redissonClient.getBucket(stockKey, StringCodec.INSTANCE).set(
//                    String.valueOf(
//                            Integer.parseInt((String) redissonClient.getBucket(stockKey, StringCodec.INSTANCE).get()) + 1
//                    )
//            );
//            redissonClient.getSet(userSetKey, StringCodec.INSTANCE).remove(userId.toString());
//            throw new RuntimeException("DB 저장 중 오류 발생, Redis 재고 복구", e);
//        }
//    }

    /**
     * TEST7
     */
    @PostConstruct
    public void loadLuaScripts() {
        // 예약 처리 Lua
        String reserveLua = """
            local stock = redis.call('GET', KEYS[1])
            if not stock then return -1 end
            stock = tonumber(stock)
            if stock <= 0 then return -1 end
            local exists = redis.call('SISMEMBER', KEYS[2], ARGV[1])
            if exists == 1 then return -2 end
            redis.call('DECR', KEYS[1])
            redis.call('SADD', KEYS[2], ARGV[1])
            return 1
        """;

        // 롤백 처리 Lua
        String rollbackLua = """
            redis.call('INCR', KEYS[1])
            redis.call('SREM', KEYS[2], ARGV[1])
            return 1
        """;

        RScript script = redissonClient.getScript(StringCodec.INSTANCE);
        reserveLuaSha = script.scriptLoad(reserveLua);
        rollbackLuaSha = script.scriptLoad(rollbackLua);
    }

//    @Transactional
    public void orderTicket(Long ticketId, Long userId) {
        String stockKey = "ticket:stock:" + ticketId;
        String userSetKey = "ticket:users:" + ticketId;

        Long result;
        try {
            result = redissonClient.getScript(StringCodec.INSTANCE).evalSha(
                    RScript.Mode.READ_WRITE,
                    reserveLuaSha,
                    RScript.ReturnType.INTEGER,
                    Arrays.asList(stockKey, userSetKey),
                    userId.toString()
            );
        } catch (Exception e) {
            throw new RuntimeException("Lua 실행 실패: " + e.getMessage(), e);
        }

        if (result == -1L) {
            throw new RuntimeException("매진된 티켓입니다.");
        }
        if (result == -2L) {
            throw new RuntimeException("이미 예매한 유저입니다.");
        }

        try {
            // 🔽 캐시된 Ticket 사용
            TicketV2 ticket = ticketCache.computeIfAbsent(ticketId, id ->
                    ticketRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."))
            );

            UserV2 user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

            UserTicketV2 userTicket = new UserTicketV2(user, ticket);
            userTicketRepository.save(userTicket);
        } catch (Exception e) {
            // Redis 복구 (Lua)
            redissonClient.getScript(StringCodec.INSTANCE).evalSha(
                    RScript.Mode.READ_WRITE,
                    rollbackLuaSha,
                    RScript.ReturnType.INTEGER,
                    Arrays.asList(stockKey, userSetKey),
                    userId.toString()
            );
            throw new RuntimeException("DB 저장 중 오류 발생, Redis 복구 수행", e);
        }
    }

    @Override
    @Transactional
    public void cancelTicket(Long ticketId, Long userId) {
        String stockKey = "ticket:stock:" + ticketId;
        String userSetKey = "ticket:users:" + ticketId;

        String luaScript =
                "local exists = redis.call('SISMEMBER', KEYS[2], ARGV[1])\n" +
                        "if exists == 0 then return -1 end\n" + // 예매 기록 없음
                        "redis.call('SREM', KEYS[2], ARGV[1])\n" +
                        "redis.call('INCR', KEYS[1])\n" +
                        "return 1";

        Long result;
        try {
            result = redissonClient.getScript(StringCodec.INSTANCE).eval(
                    RScript.Mode.READ_WRITE,
                    luaScript,
                    RScript.ReturnType.INTEGER,
                    Arrays.asList(stockKey, userSetKey),
                    userId.toString()
            );
        } catch (Exception e) {
            throw new RuntimeException("Lua 실행 실패: " + e.getMessage(), e);
        }

        if (result == -1L) {
            throw new RuntimeException("예매 기록이 없습니다.");
        }

        // Redis에서는 성공적으로 복구됐으므로, DB에서도 이력 삭제
        TicketV2 ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 티켓입니다."));
        UserV2 user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        UserTicketV2 userTicket = userTicketRepository.findByUserAndTicket(user, ticket)
                .orElseThrow(() -> new RuntimeException("예매 기록이 없습니다."));
        userTicketRepository.delete(userTicket);
    }


}
