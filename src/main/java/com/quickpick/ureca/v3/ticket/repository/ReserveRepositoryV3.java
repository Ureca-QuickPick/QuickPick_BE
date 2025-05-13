package com.quickpick.ureca.v3.ticket.repository;

import com.quickpick.ureca.v3.reserve.domain.ReserveV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepositoryV3 extends JpaRepository<ReserveV3, Long> {
}
