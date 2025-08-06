package com.nicat.authverifymicroservice.controller;

import com.nicat.authverifymicroservice.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/otp")
public class OtpController {

    private final OtpService otpService;

    @GetMapping("/send-email")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail(){
        otpService.sendOtp();
    }

    @GetMapping("/otp-verify")
    @ResponseStatus(HttpStatus.OK)
    public void otpVerify(String otpCode){
        otpService.otpVerify(otpCode);
    }

}
