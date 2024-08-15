package com.supernova.fashionnova.domain.warn;

import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warns")
public class WarnController {

    private final WarnService warnService;

    public WarnController(WarnService warnService) {
        this.warnService = warnService;
    }

    @GetMapping
    public List<WarnResponseDto> getUserWarnings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return warnService.getWarningsByUser(userDetails.getUser());
    }
}