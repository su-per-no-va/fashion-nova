package com.supernova.fashionnova.domain.warn;

import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnRepository extends JpaRepository<Warn, Long> {

    List<Warn> findByUser(User user);

    int countByUser(User user);

}
