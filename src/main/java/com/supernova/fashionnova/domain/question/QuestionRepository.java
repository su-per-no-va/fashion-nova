package com.supernova.fashionnova.domain.question;

import com.supernova.fashionnova.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByUser(User user, Pageable pageable);

}
