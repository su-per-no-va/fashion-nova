package com.supernova.fashionnova.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionRequestDto {

    @NotBlank(message = "문의 제목 입력값이 없습니다.")
    private String title;

    @NotBlank(message = "문의 내용 입력값이 없습니다.")
    private String question;

    @Pattern(regexp = "PRODUCT|RESTOCK|DELIVERY|SYSTEM|ORDER_PAYMENT|EXCHANGE_RETURN",
        message = "유효하지 않은 문의 타입입니다.")
    @NotBlank(message = "문의 타입 입력값이 없습니다.")
    private String type;

    private Long orderDetailId;

    public QuestionRequestDto(String title, String question, String type, Long orderDetailId) {
        this.title = title;
        this.question = question;
        this.type = type;
        this.orderDetailId = orderDetailId;
    }

}
