package com.supernova.fashionnova.adimn;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /** 유저 조회 등록
     *
     * @param requestDto
     * @return "회원 경고 등록 완성"
     */
    @PostMapping("/caution")
    public ResponseEntity<String> createCaution(@RequestBody WarnRequestDto requestDto) {

        adminService.createCaution(requestDto);

        return ResponseUtil.of(HttpStatus.OK,"회원 경고 등록 완성");
    }

    /** 유저 경고 삭제
     *
     * @param requestDto
     * @return "회원 경고 삭제 완료"
     */
    @DeleteMapping("/caution")
    public ResponseEntity<String> deleteCaution(@RequestBody WarnDeleteRequestDto requestDto) {

        adminService.deleteCaution(requestDto);

        return ResponseUtil.of(HttpStatus.OK,"회원 경고 삭제 완료");
    }
}
