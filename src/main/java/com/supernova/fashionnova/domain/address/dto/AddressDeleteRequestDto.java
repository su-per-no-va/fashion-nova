package com.supernova.fashionnova.domain.address.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressDeleteRequestDto {

    private Long addressId;

    public AddressDeleteRequestDto(Long addressId) {
        this.addressId = addressId;
    }

}
