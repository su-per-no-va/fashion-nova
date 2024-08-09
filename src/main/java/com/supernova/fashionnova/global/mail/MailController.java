package com.supernova.fashionnova.global.mail;

import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mails")
public class MailController {

    private final MailService mailService;

    private int number;

    /**
     * 인증 이메일 전송
     *
     * @param email
     * @return HashMap<String, Object>
     */
    @GetMapping("/send")
    public ResponseEntity<HashMap<String, Object>> mailSend(@RequestParam String email) {

        HashMap<String, Object> map = new HashMap<>();

        try {

            number = mailService.sendMail(email);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);

        } catch (Exception e) {

            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());

        }

        return ResponseUtil.of(HttpStatus.OK, map);
    }

    /**
     * 인증번호 일치 여부 확인
     *
     * @param check
     * @return "true" or "false"
     */
    @GetMapping("/check")
    public ResponseEntity<String> mailCheck(@RequestParam String check) {

        boolean isMatch = check.equals(String.valueOf(number));

        return ResponseUtil.of(HttpStatus.OK, String.valueOf(isMatch));
    }

}
