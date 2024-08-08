package com.supernova.fashionnova.domain.mileage.dto;

import com.supernova.fashionnova.domain.mileage.Mileage;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MileageResponseDto {

    private final Long usedMileage;
    private final LocalDateTime createdAt;

    public MileageResponseDto(Mileage mileage) {
        this.usedMileage = mileage.getMileage();
        this.createdAt = mileage.getCreatedAt();
    }

}
