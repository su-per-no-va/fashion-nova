package com.supernova.fashionnova.domain.mileage.dto;

import com.supernova.fashionnova.domain.order.Order;
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
