package com.supernova.fashionnova.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerRequestDto {

    private Long questionId;
    private String answer;

}
