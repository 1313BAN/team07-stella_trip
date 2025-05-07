package com.ssafy.stella_trip.security;

import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        UserDTO user = UserDTO.builder()
                .email("test@email.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .name("testUser")
                .build();
        when(userDAO.getUserByEmail("test@email.com")).thenReturn(user);
    }

    @Test
    void loginShouldAuthenticateSuccessfully() throws Exception {
        mockMvc.perform(post("/user/login")
                        .param("email", "test@email.com")
                        .param("password", "1234"))
                .andExpect(status().isOk());  // default spring login
    }
}
