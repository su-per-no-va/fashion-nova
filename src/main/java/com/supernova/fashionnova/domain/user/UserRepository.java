package com.supernova.fashionnova.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<User> findByUserName(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);

}
