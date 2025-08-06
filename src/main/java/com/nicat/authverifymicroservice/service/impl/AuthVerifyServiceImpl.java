package com.nicat.authverifymicroservice.service.impl;

import com.nicat.authverifymicroservice.dao.entity.Authority;
import com.nicat.authverifymicroservice.dao.entity.Token;
import com.nicat.authverifymicroservice.dao.entity.User;
import com.nicat.authverifymicroservice.dao.repository.AuthRepository;
import com.nicat.authverifymicroservice.dao.repository.TokenRepository;
import com.nicat.authverifymicroservice.dao.repository.UserRepository;
import com.nicat.authverifymicroservice.model.dto.request.LoginRequestDto;
import com.nicat.authverifymicroservice.model.dto.request.RefreshTokenDto;
import com.nicat.authverifymicroservice.model.dto.request.RegistrationRequestDto;
import com.nicat.authverifymicroservice.model.dto.response.LoginResponse;
import com.nicat.authverifymicroservice.model.dto.response.RefreshTokenResponse;
import com.nicat.authverifymicroservice.model.enums.Roles;
import com.nicat.authverifymicroservice.model.enums.Status;
import com.nicat.authverifymicroservice.model.exception.NotFoundException;
import com.nicat.authverifymicroservice.model.exception.UnauthorizedException;
import com.nicat.authverifymicroservice.service.AuthVerifyService;
import com.nicat.authverifymicroservice.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthVerifyServiceImpl implements AuthVerifyService {
    UserRepository userRepository;
    TokenRepository tokenRepository;
    JwtUtil jwtUtil;
    AuthenticationManager authenticationManager;
    BCryptPasswordEncoder passwordEncoder;
    AuthRepository authRepository;


    @Override
    public void register(@Valid RegistrationRequestDto registrationRequestDto) {
        log.info("authenticate method was started");
        // username mail phone number unikal olmalidir.
        User user = User.builder()
                .username(registrationRequestDto.getUsername())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .email(registrationRequestDto.getEmail())
                .phoneNumber(registrationRequestDto.getPhoneNumber())
                .status(Status.ACTIVE)
                .roles(Roles.USER)
                .authorities(List.of(authRepository.findByRoles(Roles.USER).orElseGet(() -> {
                    Authority authority = Authority.builder()
                            .roles(Roles.USER)
                            .build();
                    return authRepository.save(authority);
                })))
                .build();
        userRepository.save(user);
    }

    @Transactional
    @Override
    public LoginResponse login(@Valid LoginRequestDto loginRequestDto) {
        log.info("{}", loginRequestDto);
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                ));


        var accessToken = jwtUtil.generateAccessToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);

        revokeAllTokensOfUser(user);
        saveUserToken(user, accessToken, refreshToken);

        LoginResponse LoginResponse = new LoginResponse();
        LoginResponse.setAccessToken(accessToken);
        LoginResponse.setRefreshToken(refreshToken);

        log.info("User successfully logged in");
        return LoginResponse;
    }

    private void saveUserToken(User user, String accessToken, String refreshToken) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokensOfUser(User user) {
        List<Token> tokens = tokenRepository.findByUserAndIsLoggedOut(user, Boolean.FALSE);
        tokens.forEach(token -> token.setIsLoggedOut(Boolean.TRUE));
        tokenRepository.saveAll(tokens);
    }

    public RefreshTokenResponse refreshAccessToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        String userEmail = jwtUtil.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new UnauthorizedException("Invalid refresh token - no user found");
        }

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, user)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);
        revokeAllTokensOfUser(user);
        saveUserToken(user, newAccessToken, refreshToken);
        return new RefreshTokenResponse(newAccessToken, refreshToken);
    }
}