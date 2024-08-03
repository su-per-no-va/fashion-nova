package com.supernova.fashionnova.domain.address.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressDefaultRequestDto {

    private Long addressId;

    public AddressDefaultRequestDto(Long addressId) {
        this.addressId = addressId;
    }

}
