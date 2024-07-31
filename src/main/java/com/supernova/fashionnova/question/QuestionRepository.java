package com.supernova.fashionnova.question;

import com.supernova.fashionnova.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>  {
    Page<Question> findByUser(User user, Pageable pageable);
}
