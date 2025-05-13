package com.quickpick.ureca.v1;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TicketReservationServiceTest {

//    @Autowired private TicketRepositoryV1 ticketRepositoryV1;
//    @Autowired private UserRepository userRepository;
//    @Autowired private UserTicketRepository userTicketRepository;
//    @Autowired private ReserveServiceV1 reserveServiceV1;
//
//    @Autowired
//    private UserBulkInsertServiceV1 userBulkInsertServiceV1;
//
//    @Test
//    @DisplayName("동시에 1000개의 요청으로 100개의 티켓을 예약한다.")
//    void PessimisticReservationTest() throws InterruptedException {
//        int userCount = 30000;
//        int ticketQuantity = 100;
//
//        Ticket ticket = new Ticket("SKT 콘서트", ticketQuantity);
//        ticketRepositoryV1.save(ticket);
//
//        userBulkInsertServiceV1.insertUsersInBulk(userCount);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(userCount);
//
//        List<User> allUsers = userRepository.findAll();
//
//        for (User user : allUsers) {
//            executorService.submit(() -> {
//                try {
//                    reserveServiceV1.reserveTicket(user.getUserId(), ticket.getTicketId());
//                } catch (Exception ignored) {
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        List<UserTicket> reservations = userTicketRepository.findAll();
//        System.out.println("총 예약 수: " + reservations.size());
//        assertEquals(ticketQuantity, reservations.size());
//    }
}
