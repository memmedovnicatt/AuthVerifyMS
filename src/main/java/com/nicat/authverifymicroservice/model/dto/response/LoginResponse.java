package com.nicat.authverifymicroservice.model.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
}
