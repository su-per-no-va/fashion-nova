package com.supernova.fashionnova.address.dto;

import com.supernova.fashionnova.address.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponseDto {
    private Long id;
    private String name;
    private String recipientName;
    private String recipientNumber;
    private String zipCode;
    private String address;
    private String detail;
    private boolean defaultAddress;

    public static AddressResponseDto of(Address address) {
        return AddressResponseDto.builder()
            .id(address.getId())
            .name(address.getName())
            .recipientName(address.getRecipientName())
            .recipientNumber(address.getRecipientNumber())
            .zipCode(address.getZipCode())
            .address(address.getAddress())
            .detail(address.getDetail())
            .defaultAddress(address.isDefaultAddress())
            .build();
    }

}
