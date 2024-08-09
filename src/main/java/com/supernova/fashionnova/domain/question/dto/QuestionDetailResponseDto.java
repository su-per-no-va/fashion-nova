package com.supernova.fashionnova.domain.question.dto;

import com.supernova.fashionnova.domain.answer.dto.AnswerResponseDto;
import com.supernova.fashionnova.domain.order.dto.OrderDetailResponseDto;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionImage;
import com.supernova.fashionnova.domain.question.QuestionStatus;
import com.supernova.fashionnova.domain.question.QuestionType;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionDetailResponseDto {

    private final Long id;
    private final String title;
    private final String question;
    private final QuestionType type;
    private final QuestionStatus status;
    private final List<String> questionUrls;
    private OrderDetailResponseDto orderDetail;
    private AnswerResponseDto answer;

    public QuestionDetailResponseDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.question = question.getQuestion();
        this.type = question.getType();
        this.status = question.getStatus();
        this.questionUrls = question.getQuestionImageUrls().stream()
            .map(QuestionImage::getQuestionImageUrl).toList();
        if (this.type.equals(QuestionType.EXCHANGE_RETURN)) {
            this.orderDetail = new OrderDetailResponseDto(question.getOrderDetail());
        }
        if (question.getAnswer() != null) {
            this.answer = new AnswerResponseDto(question.getAnswer());
        }
    }

}
