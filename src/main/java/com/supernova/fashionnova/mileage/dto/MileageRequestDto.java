package com.supernova.fashionnova.mileage.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageRequestDto {

    private Long userId;
    private int mileage;

    public MileageRequestDto(Long userId, int mileage) {
        this.userId = userId;
        this.mileage = mileage;
    }

}
