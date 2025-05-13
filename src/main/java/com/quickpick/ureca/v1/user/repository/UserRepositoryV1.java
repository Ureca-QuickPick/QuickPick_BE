package com.quickpick.ureca.v1.user.repository;

import com.quickpick.ureca.v1.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryV1 extends JpaRepository<User, Long> {
}
