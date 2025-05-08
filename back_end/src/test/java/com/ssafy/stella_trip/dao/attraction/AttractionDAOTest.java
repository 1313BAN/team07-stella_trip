package com.ssafy.stella_trip.dao.attraction;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AttractionDAOTest {

    @Autowired
    private AttractionDAO attractionDAO;

    @Test
    @DisplayName("ID로 관광지 조회 테스트")
    void getAttractionById() {
        // given
        int attractionId = 56644; // 가정: DB에 존재하는 ID

        // when
        AttractionDTO attraction = attractionDAO.getAttractionById(attractionId);

        // then
        assertThat(attraction).isNotNull();
        assertThat(attraction.getAttractionId()).isEqualTo(attractionId);
    }

    @Test
    @DisplayName("시도 코드로 관광지 목록 조회 테스트")
    void getAttractionsBySidoCode() {
        // given
        int sidoCode = 1; // 가정: 서울특별시 코드

        // when
        List<AttractionDTO> attractions = attractionDAO.getAttractionsBySidoCode(sidoCode);

        // then
        assertThat(attractions).isNotEmpty();
        attractions.forEach(attraction ->
                assertThat(attraction.getSidoCode()).isEqualTo(sidoCode)
        );
    }

    @Test
    @DisplayName("시도 및 구군 코드로 관광지 목록 조회 테스트")
    void getAttractionsBySidoAndGugunCode() {
        // given
        int sidoCode = 1; // 가정: 서울특별시 코드
        int gugunCode = 1; // 가정: 종로구 코드

        // when
        List<AttractionDTO> attractions = attractionDAO.getAttractionsBySidoAndGugunCode(sidoCode, gugunCode);

        // then
        assertThat(attractions).isNotEmpty();
        attractions.forEach(attraction -> {
            assertThat(attraction.getSidoCode()).isEqualTo(sidoCode);
            assertThat(attraction.getGugunCode()).isEqualTo(gugunCode);
        });
    }

    @Test
    @DisplayName("컨텐츠 타입 ID로 관광지 목록 조회 테스트")
    void getAttractionsByContentTypeId() {
        // given
        int contentTypeId = 12; // 가정: 관광지 타입 코드

        // when
        List<AttractionDTO> attractions = attractionDAO.getAttractionsByContentTypeId(contentTypeId);

        // then
        assertThat(attractions).isNotEmpty();
        attractions.forEach(attraction ->
                assertThat(attraction.getContentTypeId()).isEqualTo(contentTypeId)
        );
    }

    @Test
    @DisplayName("존재하지 않는 ID로 관광지 조회 시 null 반환 테스트")
    void getAttractionByNonExistentId() {
        // given
        int nonExistentId = 999999; // 가정: DB에 존재하지 않는 ID

        // when
        AttractionDTO attraction = attractionDAO.getAttractionById(nonExistentId);

        // then
        assertThat(attraction).isNull();
    }

    @Test
    @DisplayName("결과가 없는 시도/구군 코드 조회 테스트")
    void getAttractionsByInvalidSidoAndGugunCode() {
        // given
        int invalidSidoCode = 999; // 가정: 존재하지 않는 시도 코드
        int invalidGugunCode = 999; // 가정: 존재하지 않는 구군 코드

        // when
        List<AttractionDTO> attractions = attractionDAO.getAttractionsBySidoAndGugunCode(
                invalidSidoCode, invalidGugunCode);

        // then
        assertThat(attractions).isEmpty();
    }
}