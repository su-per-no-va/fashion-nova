package com.supernova.fashionnova.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerRequestDto {

    private Long questionId;
    private String answer;

    public AnswerRequestDto(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

}
