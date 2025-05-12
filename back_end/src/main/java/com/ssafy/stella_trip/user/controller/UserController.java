package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.request.LoginRequestDTO;
import com.ssafy.stella_trip.user.dto.request.SignupRequestDTO;
import com.ssafy.stella_trip.user.dto.request.TokenRefreshRequestDTO;
import com.ssafy.stella_trip.user.dto.response.ActionResponseDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import com.ssafy.stella_trip.user.dto.response.SignupResponseDTO;
import com.ssafy.stella_trip.user.dto.response.TokenRefreshResponseDTO;
import com.ssafy.stella_trip.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
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
            @ApiResponse(responseCode = "200", description = "정상적으로 회원가입"),
            @ApiResponse(responseCode = "400", description = "USER-003 - 중복된 회원이 있습니다."),
            @ApiResponse(responseCode = "400", description = "USER-004 - 회원가입에 실패하였습니다."),
    })
    CommonResponse<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO, HttpServletRequest request) {
        String email = signupRequestDTO.getEmail();
        String password = signupRequestDTO.getPassword();
        String name = signupRequestDTO.getName();
        SignupResponseDTO user = userService.signup(name, email, password);
        return new CommonResponse<>(user, HttpStatus.OK);
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

    @GetMapping("/logout")
    @Operation(
            summary = "로그아웃 처리 API",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그아웃됨. 메인 페이지로 redirection")
    })
    public CommonResponse<?> logout(HttpServletResponse response) {
        // access 토큰 쿠키 삭제
        Cookie jwtCookie = new Cookie("Authorization", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        response.addCookie(jwtCookie);
        return CommonResponse.builder().status(HttpStatus.OK).build(); // 실제로는 아무 동작 안 함
    }

    @PostMapping("/signout")
    @Operation(
            summary = "회원탈퇴 처리 API",
            description = ""
    )
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 회원탈퇴 처리됨"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
            })
    CommonResponse<ActionResponseDTO> signout() {
        return new CommonResponse<ActionResponseDTO>(userService.signout(), HttpStatus.OK);
    }

}
