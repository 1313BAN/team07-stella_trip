package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.user.annotation.PasswordEncoded;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticator;

    public LoginResponseDTO login(String email, String password) {
        Authentication auth =  authenticator.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UserDTO user = userDetails.getUser();
        log.info(user.toString());
        return new LoginResponseDTO(user.getUserId(), user.getName());
    }


}
