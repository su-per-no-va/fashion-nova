package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByUser(User user, Pageable pageable);
}
