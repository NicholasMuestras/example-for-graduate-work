package ru.skypro.homework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handler for successful user logout from the system
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionId = session.getId();
            session.invalidate();
            log.info(
                    "User {} successfully logged out and session {} invalidated",
                    authentication != null ? authentication.getName() : "unknown",
                    sessionId
            );
        } else {
            log.info("Logout attempt for user {} without active session",
                    authentication != null ? authentication.getName() : "unknown");
        }

        String jsonResponse = objectMapper.writeValueAsString(
                new LogoutResponse(true, "Logout successful")
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * Class for forming JSON response on successful logout
     */
    private static class LogoutResponse {
        private final boolean result;
        private final String message;

        public LogoutResponse(boolean result, String message) {
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
