package com.ssafy.stella_trip.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ssafy.stella_trip.dao")
public class AppConfig {
}
