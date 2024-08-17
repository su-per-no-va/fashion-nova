package com.supernova.fashionnova.domain.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderDetailRequestDto {

    private int count;
    private String productName;
    private int price;

}
