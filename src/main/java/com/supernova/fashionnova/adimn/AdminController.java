package com.supernova.fashionnova.adimn;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /** 유저 전체조회
     *
     * @param page
     * @return size는 30으로 고정했음
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page) {

        List<UserResponseDto> responseDtoList = adminService.getAllUsers(page);

        return ResponseUtil.of(HttpStatus.OK, responseDtoList);
    }




}
