package com.ssafy.stella_trip.security.service;

import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.security.dto.CustomUserDetails;
import com.ssafy.stella_trip.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetails를 반아오기 위한 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    // UserDAO에서 사용자 가져오기
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userDAO.getUserByEmail(email);
        log.info(user.toString());
        if(user == null) {
            throw new UsernameNotFoundException(email+ " not found"); // 사용자가 없으면 exception 띄우기
        }
        return new CustomUserDetails(user);
    }
}
