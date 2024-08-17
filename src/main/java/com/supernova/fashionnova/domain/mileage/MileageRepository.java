package com.supernova.fashionnova.domain.mileage;

import com.supernova.fashionnova.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {

    Page<Mileage> findByUser(User user, Pageable pageable);

}
