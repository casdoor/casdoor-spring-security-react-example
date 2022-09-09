package org.casdoor.example;

import org.casbin.casdoor.service.CasdoorAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final String frontendUrl;
    private final String casdoorUrl;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter,
                          @Value("${casdoor.redirect-url}") String redirectUrl,
                          @Value("${casdoor.endpoint}") String casdoorUrl) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.frontendUrl = parseOrigin(redirectUrl);
        this.casdoorUrl = parseOrigin(casdoorUrl);
    }

    private String parseOrigin(String url) {
        int protocol = url.startsWith("https://") ? 5 : 4;
        int slash = url.indexOf('/', protocol + 3);
        return slash == -1 ? url : url.substring(0, slash);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // enable CORS and disable CSRF
        http = http.cors(corsConfig -> corsConfig
                .configurationSource(configurationSource())
        ).csrf().disable();

        // set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // set permissions on endpoints
        http.authorizeHttpRequests(authorize -> authorize
                // Our public endpoints
                .mvcMatchers("/api/redirect-url", "/api/signin").permitAll()
                // Our private endpoints
                .mvcMatchers("/api/**").authenticated()
        );

        // set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> ResponseUtils.fail(response, "unauthorized")
                )
                .and();

        // set logout handler
        http.logout(logoutConfig ->
                logoutConfig.logoutUrl("/api/logout")
                        .addLogoutHandler(new LogoutHandler() {
                            @Override
                            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

                            }
                        })
        );

        // add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

    CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Collections.singletonList(HttpHeaders.AUTHORIZATION));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList(casdoorUrl, frontendUrl));
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setExposedHeaders(Collections.emptyList());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
