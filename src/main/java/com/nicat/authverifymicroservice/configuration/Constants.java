package com.nicat.authverifymicroservice.configuration;

public class Constants {

    private Constants() {
        throw new IllegalStateException();
    }

    public static final String EN = "en";
    public static final String AZ = "az";

    public static class OtpConstants {

        public static final String[] whiteListUrls = {
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/auth/**",
                "/emails/**",
                "/otp/**",
                "/api/v1/**",
                "/docs/**",
                "/ws/**"
        };

        private OtpConstants() {
            throw new IllegalStateException();
        }
    }


    public static class Patterns {
        public static final String DATE_TIME = "dd-MM-yyyy hh:mm:ss";
        public static final String PIN_CODE = "^[0-9A-Z]{7}$";

        private Patterns() {
            throw new IllegalStateException();
        }
    }
}