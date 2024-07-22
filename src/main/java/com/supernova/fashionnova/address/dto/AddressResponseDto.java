package com.supernova.fashionnova.address.dto;

import com.supernova.fashionnova.address.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponseDto {
    private final Long id;
    private final String name;
    private final String recipientName;
    private final String recipientNumber;
    private final String zipCode;
    private final String address;
    private final String detail;
    private final boolean defaultAddress;

    public AddressResponseDto (Address address) {
        this.id = address.getId();
        this.name = address.getName();
        this.recipientName = address.getRecipientName();
        this.recipientNumber = address.getRecipientNumber();
        this.zipCode = address.getZipCode();
        this.address = address.getAddress();
        this.detail = address.getDetail();
        this.defaultAddress = address.isDefaultAddress();
    }

}
