package com.supernova.fashionnova.domain.question.dto;

import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionImage;
import com.supernova.fashionnova.domain.question.QuestionStatus;
import com.supernova.fashionnova.domain.question.QuestionType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionResponseDto {

    private final Long id;
    private final String title;
    private final String question;
    private final QuestionType type;
    private final QuestionStatus status;
    private final List<String> questionUrls;
    private final LocalDateTime createdAt;
    private final Long userId;
    private final String userName;

    public QuestionResponseDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.question = question.getQuestion();
        this.type = question.getType();
        this.status = question.getStatus();
        this.questionUrls = question.getQuestionImageUrls().stream()
            .map(QuestionImage::getQuestionImageUrl).toList();
        this.createdAt = question.getCreatedAt();
        this.userId = question.getUser().getId();
        this.userName = question.getUser().getName();
    }

}


