package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.request.LoginRequestDTO;
import com.ssafy.stella_trip.user.dto.request.SignupRequestDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/logout")
    @Operation(
            summary = "",
            description = "실제 로그아웃은 POST /logout 으로 요청하면 Spring Security에서 처리됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "정상적으로 로그아웃됨. 메인 페이지로 redirection")
    })
    public CommonResponse<?> logoutDoc() {
        return CommonResponse.builder().status(HttpStatus.FOUND).build(); // 실제로는 아무 동작 안 함
    }

}
