package com.nicat.authverifymicroservice.service;

import com.nicat.authverifymicroservice.model.dto.request.LoginRequestDto;
import com.nicat.authverifymicroservice.model.dto.request.RegistrationRequestDto;
import com.nicat.authverifymicroservice.model.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthVerifyService {
    void register(RegistrationRequestDto registrationRequestDto);

    LoginResponse login(LoginRequestDto loginRequestDto);
}