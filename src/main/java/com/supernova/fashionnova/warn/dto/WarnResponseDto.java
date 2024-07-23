package com.supernova.fashionnova.warn.dto;

import com.supernova.fashionnova.warn.Warn;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class WarnResponseDto {

    private final String detail;
    private final LocalDateTime createdAt;

   public WarnResponseDto(Warn warn) {
        this.detail = warn.getDetail();
        this.createdAt = warn.getCreatedAt();
    }
}
