package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.security.exception.InvalidTokenException;
import com.ssafy.stella_trip.security.util.JwtTokenProvider;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserRole;
import com.ssafy.stella_trip.user.dto.request.LogoutRequestDTO;
import com.ssafy.stella_trip.user.dto.response.ActionResponseDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.dto.response.SignupResponseDTO;
import com.ssafy.stella_trip.user.dto.response.TokenRefreshResponseDTO;
import com.ssafy.stella_trip.user.exception.ProfileNotFoundException;
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
        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(user.getUserId(), userDetails.getUsername(), user.getName() , user.getRole());
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
    public SignupResponseDTO signup(String name, String email, String password) {
        // password 인코딩하기
        String encodedPassword = passwordEncoder.encode(password);
        UserDTO user = UserDTO.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();
        int res = userDAO.insertUser(user);
        if(res > 0){
            return new SignupResponseDTO(user.getUserId(), user.getEmail(), user.getName());
        }
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
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        UserDTO user = userDAO.getUserByEmail(email);

        jwtTokenProvider.addToBlacklist(refreshToken);

        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(user.getUserId(), email, user.getName(), user.getRole());
        String refreshTokenRefresh = "Bearer " + jwtTokenProvider.generateRefreshToken(email);
        return new TokenRefreshResponseDTO(accessToken, refreshTokenRefresh);
    }

    /**
     * 로그아웃 Service
     * @param logoutRequestDTO 무효화할 refresh 토큰
     * @return 로그아웃 성공 여부
     */
    public ActionResponseDTO logout(LogoutRequestDTO logoutRequestDTO) {
        if (logoutRequestDTO.getRefreshToken() != null) {
            jwtTokenProvider.addToBlacklist(logoutRequestDTO.getRefreshToken());
        }

        return new ActionResponseDTO(true);
    }

    /**
     * 회원 탈퇴 Service
     * @return 회원탈퇴 성공 여부
     */
    public ActionResponseDTO signout(){
        int userId = getCurrentAuthenticatedUserId();

        userDAO.deleteUserByUserId(userId);
        return new ActionResponseDTO(true);
    }

    private int getCurrentAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JwtUserInfo)) {
            throw new ProfileNotFoundException("인증된 사용자의 정보가 아닙니다.");
        }
        JwtUserInfo userInfo = (JwtUserInfo) principal;
        return userInfo.getUserId();
    }
}
