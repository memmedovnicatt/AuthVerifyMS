package com.nicat.authverifymicroservice.service.impl;

import com.nicat.authverifymicroservice.configuration.redis.RedisRepository;
import com.nicat.authverifymicroservice.dao.entity.User;
import com.nicat.authverifymicroservice.dao.repository.UserRepository;
import com.nicat.authverifymicroservice.service.EmailSenderService;
import com.nicat.authverifymicroservice.service.OtpService;
import com.nicat.authverifymicroservice.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final RedisRepository<String, String> redisRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public void sendOtp() {
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        String email = user.getEmail();
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        redisRepository.putByKey(
                "otp:" + otpCode,
                email,
                java.time.Duration.ofMinutes(5)
        );

        emailSenderService.sendOtp(
                email,
                otpCode
        );

        //
    }

    @Override
    public void otpVerify(String otpCode) {

        String email = redisRepository.findByKey("otp:" + otpCode)
                .orElseThrow(() -> new RuntimeException("OTP code is invalid or expired"));

        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        if (!StringUtils.equals(email, user.getEmail())) {
            throw new RuntimeException("OTP code does not match the email in context");
        }

        redisRepository.putByKey(
                "otpVerified:" + email,
                "true",
                java.time.Duration.ofDays(1)
        );
        log.info("OTP verified successfully for email: {}", email);
        //verify
    }
}