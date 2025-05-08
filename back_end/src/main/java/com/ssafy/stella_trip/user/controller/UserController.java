package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.request.LoginRequestDTO;
import com.ssafy.stella_trip.user.dto.request.SignupRequestDTO;
import com.ssafy.stella_trip.user.dto.request.TokenRefreshRequestDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.dto.response.TokenRefreshResponseDTO;
import com.ssafy.stella_trip.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "로그인 처리 API",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인. 사용자 정보 반환"),
            @ApiResponse(responseCode = "401", description = "USER-001, USER-002 - 잘못된 사용자입니다."),
            @ApiResponse(responseCode = "400", description = "USER-005 - 로그인에 실패하였습니다."),
    })
    CommonResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();;
        String password = loginRequestDTO.getPassword();
        return new CommonResponse<>(userService.login(email, password), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Operation(
            summary = "회원가입 처리 API",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 회원가입. 로그인 창으로 redirection"),
            @ApiResponse(responseCode = "400", description = "USER-003 - 중복된 회원이 있습니다."),
            @ApiResponse(responseCode = "400", description = "USER-004 - 회원가입에 실패하였습니다."),
    })
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

    @PostMapping("/refresh")
    @Operation(
            summary = "access token 발급용 api",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "access token 발급됨")
    })
    public CommonResponse<TokenRefreshResponseDTO> refresh(@RequestBody TokenRefreshRequestDTO tokenRefreshRequestDTO) {
        String refreshToken = tokenRefreshRequestDTO.getRefreshToken();
        return new CommonResponse<TokenRefreshResponseDTO>(userService.refreshToken(refreshToken), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃 처리 API",
            description = "실제 로그아웃은 POST /logout 으로 요청하면 Spring Security에서 처리됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "정상적으로 로그아웃됨. 메인 페이지로 redirection")
    })
    public CommonResponse<?> logoutDoc() {
        return CommonResponse.builder().status(HttpStatus.FOUND).build(); // 실제로는 아무 동작 안 함
    }
}
