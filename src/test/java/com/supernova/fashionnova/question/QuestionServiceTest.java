package com.supernova.fashionnova.question;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderDetailRepository;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionRepository;
import com.supernova.fashionnova.domain.question.QuestionService;
import com.supernova.fashionnova.domain.question.QuestionStatus;
import com.supernova.fashionnova.domain.question.QuestionType;
import com.supernova.fashionnova.domain.question.dto.QuestionDetailResponseDto;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.upload.FileUploadUtil;
import com.supernova.fashionnova.global.upload.ImageType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private FileUploadUtil fileUploadUtil;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private QuestionService questionService;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = User.builder()
            .userName("testUser1234")
            .name("테스트유저")
            .password("test1234!#")
            .email("test@gmail.com")
            .phone("010-1234-5678")
            .build();
    }

    @Test
    @DisplayName("문의 등록 테스트")
    void addQuestionTest() {
        // Given
        QuestionRequestDto requestDto = QuestionRequestDto.builder()
            .title("Sample Title")
            .question("Sample Question")
            .type("PRODUCT")
            .build();

        List<MultipartFile> files = new ArrayList<>();

        // When
        questionService.addQuestion(user, requestDto, files);

        // Then
        verify(questionRepository).save(any(Question.class));
        verify(fileUploadUtil, never()).uploadImage(anyList(), any(ImageType.class), anyLong());
    }

    @Test
    @DisplayName("교환/반품 문의 등록 테스트 - 성공")
    void addExchangeReturnQuestionTest() {
        // Given
        Long orderDetailId = 1L;

        OrderDetail orderDetail = Mockito.mock(OrderDetail.class);
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetail));

        QuestionRequestDto requestDto = QuestionRequestDto.builder()
            .title("교환/반품 문의")
            .question("교환 또는 반품을 원합니다.")
            .type("EXCHANGE_RETURN")
            .orderDetailId(orderDetailId)
            .build();

        List<MultipartFile> files = new ArrayList<>();

        // When
        questionService.addQuestion(user, requestDto, files);

        // Then
        verify(orderDetailRepository).findById(orderDetailId);
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    @DisplayName("교환/반품 문의 등록 테스트 - 실패 (주문 내역 없음)")
    void addExchangeReturnQuestionTestWithInvalidOrder() {
        // Given
        Long orderDetailId = 1L;

        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.empty());

        QuestionRequestDto requestDto = QuestionRequestDto.builder()
            .title("교환/반품 문의")
            .question("교환 또는 반품을 원합니다.")
            .type("EXCHANGE_RETURN")
            .orderDetailId(orderDetailId)
            .build();

        List<MultipartFile> files = new ArrayList<>();

        // When & Then
        assertThrows(CustomException.class, () -> {
            questionService.addQuestion(user, requestDto, files);
        });

        verify(orderDetailRepository).findById(orderDetailId);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    @DisplayName("내 문의 조회 테스트")
    void getUserQuestionListTest() {
        // Given
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);

        Question question = Mockito.mock(Question.class);
        given(question.getUser()).willReturn(user);

        Page<Question> questionPage = new PageImpl<>(List.of(question));
        given(questionRepository.findByUser(user, pageable)).willReturn(questionPage);

        // When
        List<QuestionResponseDto> result = questionService.getUserQuestionList(user, page);

        // Then
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUserId());
        verify(questionRepository).findByUser(user, pageable);
    }

    @Test
    @DisplayName("문의 상세 조회 테스트 - 성공")
    void getUserQuestionTest() {
        // Given
        Long questionId = 1L;

        Question question = Mockito.mock(Question.class);
        given(question.getId()).willReturn(questionId);
        given(question.getTitle()).willReturn("문의 제목");
        given(question.getQuestion()).willReturn("문의 내용");
        given(question.getType()).willReturn(QuestionType.PRODUCT);
        given(question.getStatus()).willReturn(QuestionStatus.BEFORE);

        given(questionRepository.findByIdAndUser(questionId, user)).willReturn(Optional.of(question));

        // When
        QuestionDetailResponseDto result = questionService.getUserQuestion(user, questionId);

        // Then
        assertNotNull(result);
        assertEquals(questionId, result.getId());
        assertEquals("문의 제목", result.getTitle());
        assertEquals("문의 내용", result.getQuestion());
        assertEquals(QuestionType.PRODUCT, result.getType());
        assertEquals(QuestionStatus.BEFORE, result.getStatus());

        verify(questionRepository).findByIdAndUser(questionId, user);
    }

    @Test
    @DisplayName("문의 상세 조회 테스트 - 실패 (존재하지 않는 문의)")
    void getUserQuestionTestWithInvalidQuestion() {
        // Given
        Long questionId = 1L;
        given(questionRepository.findByIdAndUser(questionId, user)).willReturn(Optional.empty());

        // When & Then
        assertThrows(CustomException.class, () -> {
            questionService.getUserQuestion(user, questionId);
        });

        verify(questionRepository).findByIdAndUser(questionId, user);
    }

}
