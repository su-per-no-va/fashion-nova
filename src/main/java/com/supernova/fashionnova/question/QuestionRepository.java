package com.supernova.fashionnova.question;

import com.supernova.fashionnova.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>  {
    List<Question> findByUser(User user);
}
