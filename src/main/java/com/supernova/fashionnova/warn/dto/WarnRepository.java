package com.supernova.fashionnova.warn.dto;

import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.warn.Warn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnRepository extends JpaRepository<Warn,Long> {

    List<Warn> findByUser(User user);
}
