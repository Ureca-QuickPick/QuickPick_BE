package com.quickpick.ureca.v3.user.repository;

import com.quickpick.ureca.v3.user.domain.UserV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryV3 extends JpaRepository<UserV3, Long> {
}
