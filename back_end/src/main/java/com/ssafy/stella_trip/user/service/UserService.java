package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.security.exception.InvalidTokenException;
import com.ssafy.stella_trip.security.util.JwtTokenProvider;
import com.ssafy.stella_trip.user.annotation.PasswordEncoded;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserRole;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.dto.response.TokenRefreshResponseDTO;
import com.ssafy.stella_trip.user.exception.SignupFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticator;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 service
     * @param email 로그인용 이메일
     * @param password 로그인용 비번 (아직 encoding 안됨)
     * @return 로그인한 회원 정보
     */
    public LoginResponseDTO login(String email, String password) {
        // 인증용 객체 부르기
        Authentication auth =  authenticator.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        //인증 후 user 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal(); 
        UserDTO user = userDetails.getUser();
        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(user.getUserId(), userDetails.getUsername(), user.getRole());
        String refreshToken = "Bearer " + jwtTokenProvider.generateRefreshToken(userDetails.getUsername());
        log.info(user.toString());
        return new LoginResponseDTO(user.getUserId(), user.getName(), user.getEmail(), accessToken, refreshToken);
    }

    /**
     * 회원 가입 service
     * @param name 이름
     * @param email 이메일
     * @param password 비밀번호 (아직 encoding 안됨)
     * @return 회원가입한 유저 정보
     */
    public UserDTO signup(String name, String email, String password) {
        // password 인코딩하기
        String encodedPassword = passwordEncoder.encode(password);
        UserDTO user = UserDTO.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();
        int res = userDAO.insertUser(user);
        if(res > 0)
            return user;
        else throw new SignupFailureException("회원가입에 실패하였습니다.");
    }

    /**
     * token refresh 메서드
     * @param refreshToken refresh token
     * @return access token과 refresh token
     */
    public TokenRefreshResponseDTO refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("만료되었거나 위조된 refresh 토큰입니다.");
        }

        // TODO: 나중에 redis를 이용하여 저장하기
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        UserDTO user = userDAO.getUserByEmail(email);
        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(user.getUserId(), email, user.getRole());
        String refreshTokenRefresh = "Bearer " + jwtTokenProvider.generateRefreshToken(email);
        return new TokenRefreshResponseDTO(accessToken, refreshTokenRefresh);
    }
}
