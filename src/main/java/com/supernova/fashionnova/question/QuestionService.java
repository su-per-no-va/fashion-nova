package com.supernova.fashionnova.question;

import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 문의 등록
     *
     * @param user
     * @param requestDto
     */
    public void addQuestion(User user, QuestionRequestDto requestDto) {

        Question question = Question.builder()
            .user(user)
            .title(requestDto.getTitle())
            .question(requestDto.getQuestion())
            .type(QuestionType.valueOf(requestDto.getType()))
            .build();

        questionRepository.save(question);
    }

    /**
     * 내 문의 조회
     *
     * @param user
     * @param page
     * @return Page<QuestionResponseDto>
     */
    public Page<QuestionResponseDto> getUserQuestionPage(User user, int page) {

        Pageable pageable = PageRequest.of(page, 10);

        return questionRepository.findByUser(user, pageable).map(QuestionResponseDto::new);

    }

}
