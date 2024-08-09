package com.supernova.fashionnova.domain.answer.dto;

import com.supernova.fashionnova.domain.answer.Answer;
import lombok.Getter;

@Getter
public class AnswerResponseDto {

    private final Long id;
    private String answer;

    public AnswerResponseDto(Answer answer) {
        if (answer != null) {
            this.id = answer.getId();
            this.answer = answer.getAnswer();
        } else {
            this.id = null;
        }
    }

}
