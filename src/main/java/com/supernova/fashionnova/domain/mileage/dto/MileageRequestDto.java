package com.supernova.fashionnova.domain.mileage.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageRequestDto {

    private Long userId;
    private Long mileage;

    public MileageRequestDto(Long userId, Long mileage) {
        this.userId = userId;
        this.mileage = mileage;
    }

}
