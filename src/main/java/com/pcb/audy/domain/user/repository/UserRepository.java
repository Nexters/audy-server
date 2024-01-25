package com.pcb.audy.domain.user.repository;

import com.pcb.audy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthId(String oauthId);

    User findByEmail(String email);
}
