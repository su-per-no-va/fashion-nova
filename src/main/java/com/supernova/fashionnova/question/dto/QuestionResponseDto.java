package com.supernova.fashionnova.question.dto;

import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.question.QuestionImage;
import com.supernova.fashionnova.question.QuestionStatus;
import com.supernova.fashionnova.question.QuestionType;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionResponseDto {

    private final String title;
    private final String question;
    private final QuestionType type;
    private final QuestionStatus status;
    private final List<String> questionUrls;

    public QuestionResponseDto(Question question) {
        this.title = question.getTitle();
        this.question = question.getQuestion();
        this.type = question.getType();
        this.status = question.getStatus();
        this.questionUrls = question.getQuestionImageUrls().stream()
            .map(QuestionImage::getQuestionImageUrl).toList();
    }

}
