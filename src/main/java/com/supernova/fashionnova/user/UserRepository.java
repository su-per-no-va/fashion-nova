package com.supernova.fashionnova.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<User> findByUserName(String username);

    Optional<User> findByTid(String tid);
}
