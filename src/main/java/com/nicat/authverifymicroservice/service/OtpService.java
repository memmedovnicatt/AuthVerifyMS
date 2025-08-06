package com.nicat.authverifymicroservice.service;

import org.springframework.stereotype.Service;

@Service
public interface OtpService {
    void sendOtp();

    void otpVerify(String otpCode);
}
