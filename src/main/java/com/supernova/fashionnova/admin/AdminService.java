package com.supernova.fashionnova.admin;

import com.supernova.fashionnova.answer.Answer;
import com.supernova.fashionnova.answer.AnswerRepository;
import com.supernova.fashionnova.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.question.QuestionRepository;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final WarnRepository warnRepository;

    private static final int PAGE_SIZE = 30;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /** 유저 전체조회
     *
     * @param page
     * @return List<UserResponseDto>
     * 사이즈는 30으로 고정해놨음
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.stream()
            .map(UserResponseDto::new)
            .collect(Collectors.toList());
    }

    /** 유저 경고 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저Id로 유저를 찾을 수 없을 때
     */
    public void createCaution(WarnRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_USER));

        Warn warn = new Warn(requestDto.getDetail(),user);
        warnRepository.save(warn);
    }

    /** 유저 경고 삭제
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_WARN 경고ID로 경고를 찾을 수 없을 때
     */
    @Transactional
    public void deleteCaution(WarnDeleteRequestDto requestDto) {

        Warn warn = warnRepository.findById(requestDto.getWarnId())
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_WARN));

        warnRepository.delete(warn);
    }

    /** Q&A 답변 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_QUESTION 문의Id로 문의를 찾을 수 없을 때
     */
    public void addAnswer(AnswerRequestDto requestDto) {

        Question question = questionRepository.findById(requestDto.getQuestionId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_QUESTION));

        Answer answer = Answer.builder()
            .question(question)
            .answer(requestDto.getAnswer())
            .build();

        answerRepository.save(answer);

    }

    /**
     * Q&A 문의 전체 조회
     *
     * @param page
     * @return Page<QuestionResponseDto>
     */
    public Page<QuestionResponseDto> getQuestionPage(int page) {

        Pageable pageable = PageRequest.of(page, 10);

        return questionRepository.findAll(pageable).map(QuestionResponseDto::new);

    }

}
