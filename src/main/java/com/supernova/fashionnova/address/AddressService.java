package com.supernova.fashionnova.address;

import com.supernova.fashionnova.address.dto.AddressRequestDto;
import com.supernova.fashionnova.address.dto.AddressResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    /**
     * 배송지 추가
     *
     * @param user
     * @param requestDto
     */
    public void addAddress(User user, AddressRequestDto requestDto) {

        Address address = Address.builder()
            .user(user)
            .name(requestDto.getName())
            .recipientName(requestDto.getRecipientName())
            .recipientNumber(requestDto.getRecipientNumber())
            .zipCode(requestDto.getZipCode())
            .address(requestDto.getAddress())
            .detail(requestDto.getDetail())
            .build();

        addressRepository.save(address);
    }

    /**
     * 배송지 조회
     *
     * @param user
     */
    public List<AddressResponseDto> getAddressList(User user) {

        List<Address> addresses = addressRepository.findByUser(user);

        return addresses.stream()
            .map(AddressResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 기본 배송지 설정
     *
     * @param user
     * @param addressId
     */
    @Transactional
    public void updateDefaultAddress(User user, Long addressId) {

        List<Address> addressList = addressRepository.findByUser(user);

        for (Address address : addressList) {
            if (address.isDefaultAddress()) {
                address.updateDefaultAddress();
            }
            if (address.getId().equals(addressId)) {
                address.updateDefaultAddress();
            }
        }
    }

}
