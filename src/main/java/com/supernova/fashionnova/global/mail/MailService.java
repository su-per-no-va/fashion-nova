package com.supernova.fashionnova.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${GOOGLE_EMAIL}")
    private String senderEmail;

    private static int number;

    // 메일 만들기
    public MimeMessage CreateMail(String email) {

        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {

            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");

            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";

            message.setText(body, "UTF-8", "html");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    // 메일 보내기
    public int sendMail(String mail) {

        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }

    // 인증 번호 생성
    private void createNumber() {
        // (int)Math.random() * (최댓값 - 최소값 + 1) + 최소값
        number = (int) (Math.random() * (90000)) + 100000;
    }

}
