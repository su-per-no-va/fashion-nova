package com.supernova.fashionnova.mileage.dto;

import com.supernova.fashionnova.order.Order;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MileageResponseDto {

    private final int usedMileage;
    private final LocalDateTime createdAt;

    public MileageResponseDto(Order order) {
        this.usedMileage = order.getUsedMileage();
        this.createdAt = order.getCreatedAt();
    }

}
