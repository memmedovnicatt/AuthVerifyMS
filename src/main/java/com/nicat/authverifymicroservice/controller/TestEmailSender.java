package com.nicat.authverifymicroservice.controller;

import com.nicat.authverifymicroservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class TestEmailSender {
    private final EmailSenderService emailSenderService;

    @PostMapping()
    public ResponseEntity<Void> testSender(String email) {
        emailSenderService.sendEmail(email);
        return ResponseEntity.ok().build();
    }
}