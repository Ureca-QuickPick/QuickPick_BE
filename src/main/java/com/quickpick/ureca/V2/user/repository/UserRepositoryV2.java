package com.quickpick.ureca.V2.user.repository;

import com.quickpick.ureca.V2.user.domain.UserV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryV2 extends JpaRepository<UserV2, Long> {
}
