package ru.skypro.homework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handler for successful user authentication
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    /**
     * Handles successful authentication
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession(true);
        log.info(
                "User {} successfully authenticated and session {} created",
                authentication.getName(),
                session.getId()
        );
        String jsonResponse = objectMapper.writeValueAsString(
                new LoginResponse(true, "Authentication successful")
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * Class for forming JSON response on successful authentication
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
