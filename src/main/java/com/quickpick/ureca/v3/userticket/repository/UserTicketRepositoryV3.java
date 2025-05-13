package com.quickpick.ureca.v3.userticket.repository;

import com.quickpick.ureca.v3.userticket.domain.UserTicketV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTicketRepositoryV3 extends JpaRepository<UserTicketV3, Long> {

    boolean existsByUserId(Long userId);
}
