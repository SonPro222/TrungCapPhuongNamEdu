package org.example.trungcapphuongnam.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trungcapphuongnam.common.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    //convert json
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException ex) {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(
                    Map.of(
                            "success", false,
                            "status", 403,
                            "message", ExceptionMessage.ACCESS_DENIED,
                            "timestamp", LocalDateTime.now()
                    )
            ));
        } catch (Exception ignored) {}
    }
}