package com.ssafy.stella_trip.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 전역 설정을 위한 config 클래스
 */
@Configuration
@MapperScan(basePackages = "com.ssafy.stella_trip.dao") // MapperScan 진행
public class AppConfig {
}
