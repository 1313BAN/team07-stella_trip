package com.ssafy.stella_trip.security.config;

import com.ssafy.stella_trip.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security configuration 클래스
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    /**
     * 계층적 role을 등록하기 위한 bean
     * @return RoleHierarchy
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix() // role의 기본 prefix 설정: ROLE_
                .role("ADMIN").implies("USER").build();
    }

    /**
     * 비밀번호 인코더 bean, bcrypt 인코더를 기본으로 사용
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * rest 형식 인증을 위해 authenticationManager 빈 등록
     * @param config securityContext에서 authentication manager를 가져오기 위함
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Spring Security가 내부 생성한 것을 꺼내주는 방식
    }

    /**
     * securityFilterChain 등록용 bean
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 이후 JWT로 바꿀꺼니 csrf 비활성화
        http.csrf(customizer -> customizer.disable());
        // login form은 프론트로 분리할 예정이니 비활성화
        http.formLogin(customizer -> customizer.disable());
        // 이후 JWT로 바꿀꺼니 Authorization 헤더 비활성화
        http.httpBasic(customizer -> customizer.disable());
        // session 안쓰기
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/test/**").authenticated()
                .anyRequest().permitAll()
        );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 로그아웃은 filter단에서 처리되게끔
        http.logout(customizer -> customizer.logoutUrl("/user/logout")
                .invalidateHttpSession(true).logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID", "loginId", "Authorization")
        );
        return http.build();
    }
}
