package com.quickpick.ureca.v1.reserve.repository;

import com.quickpick.ureca.v1.reserve.domain.ReserveV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepositoryV1 extends JpaRepository<ReserveV1, Long> {
}
