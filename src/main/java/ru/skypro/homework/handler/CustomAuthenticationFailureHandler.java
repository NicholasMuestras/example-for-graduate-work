package ru.skypro.homework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handler for user authentication failure
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    /**
     * Method for handling authentication failure.
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        log.warn("Authentication failed for user {}: {}", request.getParameter("username"), exception.getMessage());

        String jsonResponse = objectMapper.writeValueAsString(
                new LoginResponse(false, "Invalid credentials")
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * Class for forming JSON response on authentication failure
     */
    private static class LoginResponse {
        private final boolean result;
        private final String message;

        public LoginResponse(boolean result, String message) {
            this.result = result;
            this.message = message;
        }

        public boolean isResult() {
            return result;
        }

        public String getMessage() {
            return message;
        }
    }
}
