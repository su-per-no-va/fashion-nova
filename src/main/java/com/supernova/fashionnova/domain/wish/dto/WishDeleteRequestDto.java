package com.supernova.fashionnova.domain.wish.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishDeleteRequestDto {

    private Long wishId;

    public WishDeleteRequestDto(Long wishId) {
        this.wishId = wishId;
    }

}
