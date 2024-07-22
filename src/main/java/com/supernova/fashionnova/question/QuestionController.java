package com.supernova.fashionnova.question;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    /** 문의 등록
     *
     * @param userDetails
     * @param requestDto
     * @return "문의 등록 성공"
     */
    @PostMapping
    public ResponseEntity<String> addQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody QuestionRequestDto requestDto) {

        questionService.addQuestion(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.CREATED,"문의 등록 성공");
    }

    /** 내 문의 조회
     *
     * @param userDetails
     * @return responseDto
     */
    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getAddressList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<QuestionResponseDto> responseDto = questionService.getUserQuestionList(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

}
