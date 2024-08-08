package com.supernova.fashionnova.domain.mileage;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {

  Optional<Mileage> findByUserId(Long userId);
}
