package com.supernova.fashionnova.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.domain.address.Address;
import com.supernova.fashionnova.domain.address.AddressRepository;
import com.supernova.fashionnova.domain.address.AddressService;
import com.supernova.fashionnova.domain.address.dto.AddressDefaultRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    @DisplayName("배송지 추가 테스트")
    void addAddressTest() {

        // given
        AddressRequestDto requestDto = AddressRequestDto.builder()
            .name("집")
            .recipientName("남현")
            .recipientNumber("010-0000-1111")
            .zipCode("12345")
            .address("사랑시 고백구 행복동")
            .detail("A동 101호")
            .build();

        User user = Mockito.mock(User.class);

        // when

        // then
        assertDoesNotThrow(() -> addressService.addAddress(user, requestDto));

    }

    @Test
    @DisplayName("배송지 목록 조회 테스트")
    void getAddressListTest() {

        // given
        User user = Mockito.mock(User.class);
        Address address = Mockito.mock(Address.class);
        given(address.getName()).willReturn("집");

        given(addressRepository.findByUserOrderByDefaultAddressDesc(user))
            .willReturn(List.of(address));

        // when
        List<AddressResponseDto> result = addressService.getAddressList(user);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        AddressResponseDto addressResponseDto = result.get(0);
        assertEquals("집", addressResponseDto.getName());

    }

    @Test
    @DisplayName("기본 배송지 설정 테스트")
    void updateDefaultAddressTest() {

        // given
        User user = Mockito.mock(User.class);
        AddressDefaultRequestDto requestDto = new AddressDefaultRequestDto(1L);

        // when
        addressService.updateDefaultAddress(user, requestDto);

        // then
        verify(addressRepository, times(1))
            .updateDefaultAddress(user.getId(), requestDto.getAddressId());

    }

}
