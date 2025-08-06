package com.nicat.authverifymicroservice.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
    void sendEmail(String email);

    void sendOtp(String email, String otpCode);
}
