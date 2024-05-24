package jjapra.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjapra.app.config.jwt.JwtAuthenticationFilter;
import jjapra.app.config.jwt.JwtProvider;
import jjapra.app.config.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;

    private static void corsAllow(CorsConfigurer<HttpSecurity> corsCustomizer) {
        corsCustomizer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:3000", "https://127.0.0.1:5500"));
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);

            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

            return configuration;
        });
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .cors(WebSecurityConfig::corsAllow)
                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                new MvcRequestMatcher(introspector, "/login"),
                                new MvcRequestMatcher(introspector, "/join"),
                                new MvcRequestMatcher(introspector, "/h2-console/**"),
                                new MvcRequestMatcher(introspector, "/h2-console")).permitAll())
//                        .requestMatchers(HttpMethod.GET, "/post",  "/post/random", "/post/{title}").hasAnyRole("GUEST", "ACTIVE", "ADMIN")
//                        .requestMatchers("/post", "/post/upload", "/post/{title}").hasAnyRole("ACTIVE", "ADMIN")
//                        .requestMatchers("/admin", "/admin/{id}").hasRole("ADMIN"))
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), LoginFilter.class)

                //세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
