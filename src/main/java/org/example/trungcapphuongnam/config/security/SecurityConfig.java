package org.example.trungcapphuongnam.config.security;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.example.trungcapphuongnam.common.constant.RoleConstant;
import org.example.trungcapphuongnam.config.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oauth2SuccessHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oauth2SuccessHandler = oauth2SuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())

                .httpBasic(httpBasic -> httpBasic.disable())

                .logout(logout -> logout.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, SecurityUrls.URL_ALL).permitAll()

                        .requestMatchers(SecurityUrls.PUBLIC_URLS).permitAll()

                        .requestMatchers(SecurityUrls.AUTHENTICATED_URLS).authenticated()

                        .requestMatchers(SecurityUrls.ADMIN_URLS)
                        .hasRole(RoleConstant.ADMIN)

                        .requestMatchers(SecurityUrls.STUDENT_URLS)
                        .hasRole(RoleConstant.SINH_VIEN)

                        .requestMatchers(SecurityUrls.LECTURER_URLS)
                        .hasRole(RoleConstant.GIAO_VIEN)

                        .requestMatchers(SecurityUrls.ACADEMIC_OFFICE_URLS)
                        .hasRole(RoleConstant.DAO_TAO)

                        .anyRequest().permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri(SecurityUrls.OAUTH2_AUTHORIZATION_BASE_URI)
                        )
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri(SecurityUrls.OAUTH2_REDIRECT_BASE_URI)
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler((request, response, exception) ->
                                response.sendRedirect(frontendUrl + SecurityUrls.OAUTH2_FAILURE_REDIRECT)
                        )
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}