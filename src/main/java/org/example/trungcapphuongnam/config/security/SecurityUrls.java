package org.example.trungcapphuongnam.config.security;

public class SecurityUrls {

    private SecurityUrls() {}

    public static final String URL_ALL = "/**";

    public static final String[] PUBLIC_URLS = {
            "/",
            "/error",

            "/api/auth/**",
            "/api/v1/auth/**",

            "/oauth2/**",
            "/login/oauth2/**",

            "/swagger-ui/**",
            "/v3/api-docs/**",

            "/public/**"
    };

    public static final String[] AUTHENTICATED_URLS = {
            "/profile/**",
            "/notification/**"
    };

    public static final String[] STUDENT_URLS = {
            "/student/**",
            "/dang-ky-mon/**"
    };

    public static final String[] LECTURER_URLS = {
            "/lecturer/**",
            "/grade/**"
    };

    public static final String[] ACADEMIC_OFFICE_URLS = {
            "/api/dao-tao/**",
            "/api/chuong-trinh/**",

            "/dao-tao/**",
            "/chuong-trinh/**",
            "/course-management/**"
    };

    public static final String[] ADMIN_URLS = {
            "/api/he-thong/**",

            "/admin/**",
            "/user-management/**"
    };

    public static final String OAUTH2_AUTHORIZATION_BASE_URI = "/oauth2/authorization";

    public static final String OAUTH2_REDIRECT_BASE_URI = "/login/oauth2/code/*";

    public static final String OAUTH2_FAILURE_REDIRECT = "/login?oauth2_error=true";
}