package com.supernova.fashionnova.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionController;
import com.supernova.fashionnova.domain.question.QuestionService;
import com.supernova.fashionnova.domain.question.QuestionType;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionService questionService;

    private final String baseUrl = "/questions";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @BeforeEach
    void setUp() {

        // Given a mock UserDetailsImpl
        given(userDetails.getUsername()).willReturn("user");
        given(userDetails.getUser()).willReturn(new User(
            "testUSer",
            "Test1234!@",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678"
        ));

        // Set the security context
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("문의 등록 성공 테스트")
    void addQuestionTest() throws Exception {
        // given
        QuestionRequestDto requestDto = QuestionRequestDto.builder()
            .title("문의 제목")
            .question("문의 내용")
            .type("PRODUCT")
            .build();
        MockMultipartFile requestDtoFile =
            new MockMultipartFile("requestDto", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestDto));
        MockMultipartFile file =
            new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);

        doNothing().when(questionService)
            .addQuestion(any(User.class), any(QuestionRequestDto.class), anyList());

        // when
        ResultActions result = mockMvc.perform(multipart(baseUrl)
            .file(file)
            .file(requestDtoFile)
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isCreated())
            .andExpect(content().string("문의 등록 성공"));
        verify(questionService).addQuestion(any(User.class), any(QuestionRequestDto.class), anyList());
    }

    @Test
    @DisplayName("내 문의 조회 테스트")
    void getUserQuestionPageTest() throws Exception {
        User user = userDetails.getUser();
        int page = 0;

        // Given
        Question question1 = new Question(user, "문의1", "문의내용1", QuestionType.PRODUCT);
        Question question2 = new Question(user, "문의2", "문의내용2", QuestionType.DELIVERY);

        List<QuestionResponseDto> responseDtoList = Arrays.asList(
            new QuestionResponseDto(question1),
            new QuestionResponseDto(question2)
        );

        // When
        when(questionService.getUserQuestionList(user, page)).thenReturn(responseDtoList);

        // Then
        mockMvc.perform(get(baseUrl)
                .param("page", String.valueOf(page))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(questionService).getUserQuestionList(user, page);
    }

}
