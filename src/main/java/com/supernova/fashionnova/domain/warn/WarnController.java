package com.supernova.fashionnova.domain.warn;

import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warns")
public class WarnController {

    private final WarnService warnService;

    /**
     * 본인 경고 조회
     *
     * @param userDetails
     * @return List<WarnResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<WarnResponseDto>> getUserWarnings(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<WarnResponseDto> responseDtoList = warnService.getWarningsByUser(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDtoList);
    }

}
