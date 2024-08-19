/*
package com.supernova.fashionnova.mileage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.mileage.Mileage;
import com.supernova.fashionnova.domain.mileage.MileageController;
import com.supernova.fashionnova.domain.mileage.MileageService;
import com.supernova.fashionnova.domain.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MileageController.class)
class MileageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/mileages";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @MockBean
    private MileageService mileageService;

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
    @DisplayName("마일리지 내역 조회 테스트")
    public void getMileageHistoryListTest() throws Exception {
        // given
        int page = 0;

        Mileage mileage = Mockito.mock(Mileage.class);
        MileageResponseDto responseDto = new MileageResponseDto(mileage);

        List<MileageResponseDto> responseDtoList = List.of(responseDto);

        given(mileageService.getMileageHistoryList(any(User.class), eq(page)))
            .willReturn(responseDtoList);

        // when
        ResultActions result = mockMvc.perform(get(baseUrl)
            .param("page", String.valueOf(page))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)));

        verify(mileageService).getMileageHistoryList(any(User.class), eq(page));
    }

}

 */