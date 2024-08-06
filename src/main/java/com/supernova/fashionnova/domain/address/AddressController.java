package com.supernova.fashionnova.domain.address;

import com.supernova.fashionnova.domain.address.dto.AddressDefaultRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressDeleteRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    /**
     * 배송지 추가
     *
     * @param userDetails
     * @param requestDto
     * @return "배송지 추가 성공"
     */
    @PostMapping
    public ResponseEntity<String> addAddress(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody AddressRequestDto requestDto) {

        addressService.addAddress(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.CREATED,"배송지 추가 성공");
    }

    /**
     * 배송지 조회
     *
     * @param userDetails
     * @return responseDto
     */
    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAddressList(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<AddressResponseDto> responseDto = addressService.getAddressList(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 기본 배송지 설정
     *
     * @param userDetails
     * @param requestDto
     * @return "기본 배송지 설정 성공"
     */
    @PutMapping
    public ResponseEntity<String> updateDefaultAddress(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody AddressDefaultRequestDto requestDto) {

        addressService.updateDefaultAddress(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.OK,"기본 배송지 설정 성공");
    }

    /**
     * 배송지 삭제
     *
     * @param userDetails
     * @param requestDto
     * @return "배송지 삭제 성공"
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAddress(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody AddressDeleteRequestDto requestDto) {

        addressService.deleteAddress(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.OK,"배송지 삭제 성공");
    }

}
