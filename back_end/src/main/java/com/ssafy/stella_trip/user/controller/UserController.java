package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.request.LoginRequestDTO;
import com.ssafy.stella_trip.user.dto.request.SignupRequestDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    CommonResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();;
        String password = loginRequestDTO.getPassword();
        return new CommonResponse<>(userService.login(email, password), HttpStatus.OK);
    }

    @PostMapping("/signup")
    CommonResponse<?> signup(@RequestBody SignupRequestDTO signupRequestDTO, HttpServletRequest request) {
        String email = signupRequestDTO.getEmail();
        String password = signupRequestDTO.getPassword();
        String name = signupRequestDTO.getName();
        UserDTO user = userService.signup(name, email, password);
        return CommonResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", request.getContextPath() +  "/")
                .build();
    }
}
