package com.supernova.fashionnova.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

}
