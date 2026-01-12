package com.maksym.todoapi.security;

import com.maksym.todoapi.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class UserIdInterceptor implements HandlerInterceptor {
    private static final String HEADER = "X-User-Id";

    @Override
    public  boolean preHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler) {
        String raw = request.getHeader(HEADER);

        if (raw == null || raw.isBlank()) {
            throw new UnauthorizedException("X-User-Id header is required");
        }

        try {
            UserContext.set(UUID.fromString(raw));
            return true;
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException("X-User-Id must be a valid UUID");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse reponse, Object handler, Exception ex) {
        UserContext.clear();
    }
}
