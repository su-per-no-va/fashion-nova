package com.supernova.fashionnova.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class UserUpdateRequestDto {

    @NotBlank(message = "ID 입력값이 없습니다.")
    @Length(min = 4, max = 100, message = "ID는 최소 4글자, 최대 100글자입니다.")
    private String userName;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "비밀번호는 영문, 숫자, 특수문자 포함 8자리 이상이어야 합니다.")
    @NotBlank(message = "비밀번호의 입력 값이 없습니다.")
    @Length(min = 8, max = 100, message = "비밀번호는 최소 8글자, 최대 100글자 입니다.")
    private String password;
    @NotBlank(message = "이름 입력값이 없습니다.")
    @Length(min = 2, max = 50, message = "이름은 최소 2글자, 최대 50글자 입니다")
    private String name;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력값이 없습니다.")
    private String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$",
        message = "휴대폰 형식에 맞지 않습니다. 휴대폰 형식: 010-****-**** ")
    @NotBlank(message = "휴대폰 입력값이 없습니다.")
    private String phone;

}
