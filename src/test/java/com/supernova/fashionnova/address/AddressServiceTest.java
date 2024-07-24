package com.supernova.fashionnova.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.address.dto.AddressRequestDto;
import com.supernova.fashionnova.address.dto.AddressResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
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
    void addAddressTest() {
        // given
        AddressRequestDto requestDto = new AddressRequestDto("집",
            "남현",
            "010-0000-1111",
            "12345",
            "사랑시 고백구 행복동",
            "A동 101호");

        User user = Mockito.mock(User.class);

        // when


        // then
        assertDoesNotThrow(() -> addressService.addAddress(user, requestDto));
    }

    @Test
    void getAddressListTest() {
        // given
        User user = Mockito.mock(User.class);
        Address address = Mockito.mock(Address.class);
        given(address.getName()).willReturn("집");

        given(addressRepository.findByUserOrderByDefaultAddressDesc(user)).willReturn(List.of(address));

        // when
        List<AddressResponseDto> result = addressService.getAddressList(user);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        AddressResponseDto addressResponseDto = result.get(0);
        assertEquals("집", addressResponseDto.getName());
    }

    @Test
    void updateDefaultAddressTest() {
        // given
        User user = Mockito.mock(User.class);
        Long addressId = 1L;

        // when
        addressService.updateDefaultAddress(user, addressId);

        // then
        verify(addressRepository, times(1)).updateDefaultAddress(user.getId(), addressId);
    }
}