package com.supernova.fashionnova.domain.address;

import com.supernova.fashionnova.domain.address.dto.AddressDefaultRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressDeleteRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
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
     * @return List<AddressResponseDto>
     */
    public List<AddressResponseDto> getAddressList(User user) {

        List<Address> addresses = addressRepository.findByUserOrderByDefaultAddressDesc(user);

        return addresses.stream()
            .map(AddressResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 기본 배송지 설정
     *
     * @param user
     * @param requestDto
     */
    @Transactional
    public void updateDefaultAddress(User user, AddressDefaultRequestDto requestDto) {

        addressRepository.updateDefaultAddress(user.getId(), requestDto.getAddressId());
    }

    /**
     * 배송지 삭제
     *
     * @param user
     * @param requestDto
     * @throws CustomException NOT_FOUND_ADDRESS
     * @throws CustomException INVALID_ADDRESS
     */
    @Transactional
    public void deleteAddress(User user, AddressDeleteRequestDto requestDto) {

        Address address = getAddress(requestDto.getAddressId());
        validateUser(user, address);

        addressRepository.delete(address);
    }

    private Address getAddress(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_ADDRESS)
        );
    }

    private void validateUser(User user, Address address) {
        if(!address.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.INVALID_ADDRESS);
        }
    }

}
