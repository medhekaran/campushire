package com.campushire.app.util;

import com.campushire.app.exception.ForbiddenException;
import com.campushire.app.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;

public final class SessionUtil {

    private SessionUtil() {
    }

    // Returns the logged-in user's id, or throws 401 (not logged in) / 403 (wrong role)
    public static Long requireRole(HttpSession session, String... allowedRoles) {
        Object userId = session.getAttribute("userId");
        Object role = session.getAttribute("role");

        if (userId == null) {
            throw new UnauthorizedException("Please log in first");
        }
        if (!Arrays.asList(allowedRoles).contains(role)) {
            throw new ForbiddenException("You do not have permission to do this");
        }
        return (Long) userId;
    }

    public static String getRole(HttpSession session) {
        return (String) session.getAttribute("role");
    }
}