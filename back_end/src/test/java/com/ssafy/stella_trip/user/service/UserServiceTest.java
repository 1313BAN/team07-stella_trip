package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.dao.UserDAO;
import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.response.LoginResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void loginTest() {
        // given
        String email = "test@email.com";
        String rawPassword = "test";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        UserDTO user = UserDTO.builder()
                .userId(1)
                .email(email)
                .password(encodedPassword)
                .name("testUser")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        when(userDAO.getUserByEmail(email)).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        // when
        LoginResponseDTO response = userService.login(email, rawPassword);

        // then
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("testUser", response.getName());
    }

    @Test
    void peTest() {
        String encodedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("1234");
        System.out.println(encodedPassword);
    }
}