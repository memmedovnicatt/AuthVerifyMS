package com.nicat.authverifymicroservice.configuration.security;

import com.nicat.authverifymicroservice.configuration.Constants;
import com.nicat.authverifymicroservice.configuration.redis.RedisRepository;
import com.nicat.authverifymicroservice.dao.entity.User;
import com.nicat.authverifymicroservice.dao.repository.UserRepository;
import com.nicat.authverifymicroservice.utils.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OtpFilter extends OncePerRequestFilter {
    UserRepository userRepository;
    RedisRepository<String, String> redisRepository;
    AntPathMatcher pathMatcher;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        boolean matches = Arrays.stream(Constants.OtpConstants.whiteListUrls)
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));

        if (matches) {
            log.info("Request URI is in whitelist, skipping OTP filter");
            filterChain.doFilter(request, response);
            return;
        }

        String username = SecurityUtil.getCurrentUsername();
        if (username == null || username.isEmpty()) {
            log.error("Username is null or empty, skipping OTP filter");
            return;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("Email is null or empty for user: {}", username);
            return;
        }

        Optional<String> email = redisRepository.findByKey("otpVerified:" + user.getEmail());

        if (!email.isPresent()) {
            log.error("OTP not verified for user: {}", username);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OTP not verified");
            return;
        }

        log.info("OTP verified for user: {}", username);

        filterChain.doFilter(request, response);
    }
}
