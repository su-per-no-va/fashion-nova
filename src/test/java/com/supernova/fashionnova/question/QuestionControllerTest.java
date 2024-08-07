package com.supernova.fashionnova.question;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.question.QuestionController;
import com.supernova.fashionnova.domain.question.QuestionService;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

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

    /*
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
        Question question1 = Mockito.mock(Question.class);
        Question question2 = Mockito.mock(Question.class);

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

     */

}
