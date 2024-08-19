/*
package com.supernova.fashionnova.address;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.address.Address;
import com.supernova.fashionnova.domain.address.AddressController;
import com.supernova.fashionnova.domain.address.AddressService;
import com.supernova.fashionnova.domain.address.dto.AddressDefaultRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/addresses";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @MockBean
    private AddressService addressService;

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
    @DisplayName("배송지 추가 테스트")
    void addAddressTest() throws Exception {

        // given
        User user = userDetails.getUser();
        AddressRequestDto requestDto = AddressRequestDto.builder()
            .name("집")
            .recipientName("남현")
            .recipientNumber("010-0000-1111")
            .zipCode("12345")
            .address("사랑시 고백구 행복동")
            .detail("A동 101호")
            .build();
        doNothing().when(addressService).addAddress(eq(user), eq(requestDto));

        // when * then
        mockMvc.perform(post(baseUrl)
                .with(csrf())
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isCreated(),
                content().string("배송지 추가 성공")
            );

    }

    @Test
    @DisplayName("배송지 목록 조회 테스트")
    void getAddressListTest() throws Exception {

        // given
        User user = userDetails.getUser();
        List<AddressResponseDto> responseDtoList = Arrays.asList(
            new AddressResponseDto(
                new Address(user, "집", "남현", "010-0000-1111", "12345", "사랑시 고백구 행복동", "A동 101호")),
            new AddressResponseDto(
                new Address(user, "회사", "남현", "010-0000-2222", "54321", "이별시 그리움구 고통동", "B동 102호"))
        );

        // when
        when(addressService.getAddressList(user)).thenReturn(responseDtoList);

        // then
        mockMvc.perform(get(baseUrl)
                .with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(responseDtoList.size()),
                jsonPath("$[0].name").value("집"),
                jsonPath("$[0].recipientName").value("남현"),
                jsonPath("$[0].recipientNumber").value("010-0000-1111"),
                jsonPath("$[0].zipCode").value("12345"),
                jsonPath("$[0].address").value("사랑시 고백구 행복동"),
                jsonPath("$[0].detail").value("A동 101호"),
                jsonPath("$[0].defaultAddress").value(false),
                jsonPath("$[1].name").value("회사"),
                jsonPath("$[1].recipientName").value("남현"),
                jsonPath("$[1].recipientNumber").value("010-0000-2222"),
                jsonPath("$[1].zipCode").value("54321"),
                jsonPath("$[1].address").value("이별시 그리움구 고통동"),
                jsonPath("$[1].detail").value("B동 102호"),
                jsonPath("$[1].defaultAddress").value(false)
            );

    }

    @Test
    @DisplayName("기본 배송지 설정 테스트")
    void updateDefaultAddressTest() throws Exception {

        // given
        User user = userDetails.getUser();
        AddressDefaultRequestDto requestDto = new AddressDefaultRequestDto(1L);
        doNothing().when(addressService).updateDefaultAddress(eq(user), eq(requestDto));

        // when * then
        mockMvc.perform(put(baseUrl)
                .with(csrf())
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                content().contentType("text/plain;charset=UTF-8"),
                content().string("기본 배송지 설정 성공")
            );

    }

}

 */