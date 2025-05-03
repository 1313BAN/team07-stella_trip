package com.ssafy.stella_trip.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix() // role의 기본 prefix 설정: ROLE_
                .role("ADMIN").implies("USER").role("USER").implies("GUEST").build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // rest 형식 인증을 위해 authenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Spring Security가 내부 생성한 것을 꺼내주는 방식
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable());
        http.formLogin(customizer -> customizer.disable());
        http.httpBasic(customizer -> customizer.disable());


        http.authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
        );


        http.logout(customizer -> customizer.logoutUrl("/user/logout")
                .invalidateHttpSession(true).logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID", "loginId")
        );
        return http.build();
    }
}
