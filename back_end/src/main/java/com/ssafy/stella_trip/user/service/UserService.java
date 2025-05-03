package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.dao.UserDAO;
import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.user.annotation.PasswordEncoded;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserRole;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.exception.SignupFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticator;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    public LoginResponseDTO login(String email, String password) {
        Authentication auth =  authenticator.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UserDTO user = userDetails.getUser();
        log.info(user.toString());
        return new LoginResponseDTO(user.getUserId(), user.getName(), user.getEmail());
    }

    public UserDTO signup(String name, String email, String password) {
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


}
