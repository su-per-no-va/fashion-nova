package com.supernova.fashionnova.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestResponseDto {

    private String testName;
    private Long testCode;
    private String testContent;
    private String testNaEun;

}
