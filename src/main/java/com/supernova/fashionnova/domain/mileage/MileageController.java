package com.supernova.fashionnova.domain.mileage;

import com.supernova.fashionnova.domain.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileages")
public class MileageController {

    private final MileageService mileageService;

    /**
     * 마일리지 내역 조회
     *
     * @param userDetails
     * @param page
     * @return List<MileageResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<MileageResponseDto>> getMileageHistoryList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        List<MileageResponseDto> responseDto = mileageService.getMileageHistoryList(userDetails.getUser(), page);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

}
