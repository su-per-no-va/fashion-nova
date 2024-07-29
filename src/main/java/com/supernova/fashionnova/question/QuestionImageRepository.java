package com.supernova.fashionnova.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionImageRepository extends JpaRepository<QuestionImage,Long> {

    List<QuestionImage> findAllByQuestion(Question question);

}
