package com.supernova.fashionnova.review;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    Optional<ReviewImage> findByReview(Review review);

}
