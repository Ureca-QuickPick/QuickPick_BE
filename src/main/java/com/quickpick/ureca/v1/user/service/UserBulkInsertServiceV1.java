package com.quickpick.ureca.v1.user.service;

import com.quickpick.ureca.v1.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBulkInsertServiceV1 {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertUsersInBulk(int userCount) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            users.add(new User("user" + i));
        }

        int batchSize = 100;
        for (int i = 0; i < users.size(); i++) {
            entityManager.persist(users.get(i));
            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
