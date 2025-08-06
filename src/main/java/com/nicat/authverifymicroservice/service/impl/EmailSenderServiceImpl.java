package com.nicat.authverifymicroservice.service.impl;

import com.nicat.authverifymicroservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String email) {
        log.info("sendEmail was started");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        String myEmail = "agayevarenka355@gmail.com";
        msg.setFrom(myEmail);
        msg.setSubject("GluonApp");
        msg.setText("for testing");
        javaMailSender.send(msg);
    }

    @Override
    public void sendOtp(String email, String otpCode) {
        log.info("sendOtp was started");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setText(otpCode);
        String myEmail = "Your OTP code is : %s".formatted(otpCode);
        javaMailSender.send(msg);
    }
}