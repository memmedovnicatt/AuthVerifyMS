package com.nicat.authverifymicroservice.controller;

import com.nicat.authverifymicroservice.model.dto.request.LoginRequestDto;
import com.nicat.authverifymicroservice.model.dto.request.RegistrationRequestDto;
import com.nicat.authverifymicroservice.model.dto.response.LoginResponse;
import com.nicat.authverifymicroservice.service.AuthVerifyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthVerifyController {
    AuthVerifyService authVerifyService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequestDto registrationRequestDto) {
        authVerifyService.register(registrationRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponse loginResponse = authVerifyService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponse);
    }
}