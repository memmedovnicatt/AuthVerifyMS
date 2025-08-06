package com.nicat.authverifymicroservice.utils;

import com.nicat.authverifymicroservice.dao.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return "";
    }


    public static String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getEmail();
        }
        return "";
    }
}