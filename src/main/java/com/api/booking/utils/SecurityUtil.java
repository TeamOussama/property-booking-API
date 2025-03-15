package com.api.booking.utils;

import com.api.booking.dto.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    /**
     * Returns the currently authenticated user.
     *
     * @return the authenticated user as JwtUserDetails.
     * @throws IllegalStateException if no user is authenticated or the principal is not of type JwtUserDetails.
     */
    public static JwtUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtUserDetails) {
            return (JwtUserDetails) principal;
        } else {
            throw new IllegalStateException("Authenticated user is not of type JwtUserDetails.");
        }
    }
}
