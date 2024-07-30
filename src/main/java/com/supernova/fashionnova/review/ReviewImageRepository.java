package com.supernova.fashionnova.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findAllByReview(Review review);

    List<ReviewImage> findAllByReviewId(Long id);
}
