package com.supernova.fashionnova.domain.question;

import com.supernova.fashionnova.domain.question.dto.QuestionDetailResponseDto;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 문의 등록
     *
     * @param userDetails
     * @param requestDto
     * @param file
     * @return "문의 등록 성공"
     */
    @PostMapping
    public ResponseEntity<String> addQuestion(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestPart QuestionRequestDto requestDto,
        @RequestPart(required = false) List<MultipartFile> file) {

        questionService.addQuestion(userDetails.getUser(), requestDto, file);

        return ResponseUtil.of(HttpStatus.CREATED, "문의 등록 성공");
    }

    /**
     * 내 문의 목록 조회
     *
     * @param userDetails
     * @param page
     * @return List<QuestionResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getUserQuestionList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(value = "page", defaultValue = "0") int page) {

        List<QuestionResponseDto> responseDto = questionService.getUserQuestionList(userDetails.getUser(), page);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 문의 상세 조회
     *
     * @param userDetails
     * @param questionId
     * @return QuestionDetailResponseDto
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetailResponseDto> getUserQuestionList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long questionId) {

        QuestionDetailResponseDto responseDto = questionService.getUserQuestion(userDetails.getUser(), questionId);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

}
