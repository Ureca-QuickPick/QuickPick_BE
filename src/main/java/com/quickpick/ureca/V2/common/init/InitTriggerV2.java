//package com.quickpick.ureca.V2.common.init;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//@Profile("local") // 배포환경에서는 작동 안 하도록
//@RequiredArgsConstructor
//public class InitTriggerV2 implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final InitServiceV2 initService;
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        LocalDateTime reserveDate = LocalDateTime.now();
//        LocalDateTime startDate = reserveDate.plusDays(1);
//        // ticketCount, userCount는 필요에 따라 조정
//        initService.initialize(3000, 10000, startDate, reserveDate);
//    }
//
//}
