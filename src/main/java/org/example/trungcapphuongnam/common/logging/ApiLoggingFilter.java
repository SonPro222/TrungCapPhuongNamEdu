package org.example.trungcapphuongnam.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingFilter.class);

    private static final int MAX_BODY_LENGTH = 3000;

    private static final Set<String> SKIP_PATHS = Set.of(
            "/actuator",
            "/swagger",
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (SKIP_PATHS.stream().anyMatch(uri::startsWith)) {
            return true;
        }

        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            return true;
        }

        // Bỏ log GET thông thường; chỉ giữ GET cho dữ liệu nhạy cảm (điểm, học phí)
        if ("GET".equalsIgnoreCase(method)) {
            return !uri.contains("/diem") && !uri.contains("/hoc-phi");
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString();

        MDC.put("traceId", traceId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long durationMs = System.currentTimeMillis() - startTime;

            String requestBody = getRequestBody(wrappedRequest);
            String responseBody = getResponseBody(wrappedResponse);

            log.info(
                    "API_LOG traceId={} actor={} method={} uri={} query={} status={} durationMs={} ip={} userAgent={} requestBody={} responseBody={}",
                    traceId,
                    getActor(),
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    wrappedResponse.getStatus(),
                    durationMs,
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    maskSensitiveData(requestBody),
                    maskSensitiveData(responseBody)
            );

            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();

        if (content.length == 0) {
            return "";
        }

        return truncate(new String(content, StandardCharsets.UTF_8));
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();

        if (content.length == 0) {
            return "";
        }

        return truncate(new String(content, StandardCharsets.UTF_8));
    }

    private String truncate(String value) {
        if (value == null) {
            return "";
        }

        if (value.length() <= MAX_BODY_LENGTH) {
            return value;
        }

        return value.substring(0, MAX_BODY_LENGTH) + "...[TRUNCATED]";
    }

    private String maskSensitiveData(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value
                .replaceAll("(?i)(\"password\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"matKhau\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"token\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"accessToken\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"refreshToken\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"secret\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"authorization\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"mailPassword\"\\s*:\\s*\")[^\"]*(\")", "$1***$2");
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private String getActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        return authentication.getName();
    }
}