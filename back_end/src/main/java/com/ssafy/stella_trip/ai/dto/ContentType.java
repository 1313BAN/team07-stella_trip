package com.ssafy.stella_trip.ai.dto;

import java.util.Arrays;

public enum ContentType {
    TOURIST_SPOT(12, "관광지"),
    CULTURAL_FACILITY(14, "문화시설"),
    EVENT(15, "축제공연행사"),
    TRAVEL_COURSE(25, "여행코스"),
    LEISURE(28, "레포츠"),
    ACCOMMODATION(32, "숙박"),
    SHOPPING(38, "쇼핑"),
    TOURIST_SPOT_ALT(39, "관광지");

    private final int id;
    private final String name;

    ContentType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getNameById(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .map(ContentType::getName)
                .findFirst()
                .orElse("알 수 없음");
    }

    public String getName() {
        return name;
    }
}
