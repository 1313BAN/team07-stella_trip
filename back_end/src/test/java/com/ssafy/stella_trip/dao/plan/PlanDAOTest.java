package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlanDAOTest {

    @Autowired
    private PlanDAO planDAO;

    @Test
    @DisplayName("getPlanById로 PlanDTO를 정상적으로 조회한다")
    void testGetPlanById() {
        // given
        int planId = 1; // 테스트 데이터베이스에 존재하는 plan_id 값

        // when
        PlanDTO plan = planDAO.getPlanById(planId, 1);

        // then
        assertThat(plan).isNotNull();
        assertThat(plan.getPlanId()).isEqualTo(planId);
        assertThat(plan.getTitle()).isNotEmpty();
        assertThat(plan.getWriters()).isNotEmpty();
        assertThat(plan.getTags()).isNotEmpty();

        // Lazy loading 확인
        assertThat(plan.getRoutes()).isNotNull();
        for (RouteDTO route : plan.getRoutes()) {
            assertThat(route.getRouteId()).isGreaterThan(0);
            assertThat(route.getAttraction()).isNotNull();
            assertThat(route.getAttraction().getAttractionId()).isGreaterThan(0);
        }

        // 결과값 출력
        System.out.println("PlanDTO: " + plan);
        System.out.println("Routes: " + plan.getRoutes());
    }

    @Test
    @DisplayName("getPlansByCondition로 조건에 맞는 PlanDTO 리스트를 조회한다")
    void testGetPlansByCondition() {
        // given
        int page = 1;
        int size = 10;
        int offset = (page - 1) * size;
        String search = ""; // 테스트 데이터베이스에 존재하는 제목 검색어
        String userName = ""; // 테스트 데이터베이스에 존재하는 사용자 이름
        int duration = 3; // 여행 기간
        String sort = "recent"; // 정렬 기준

        // when
        List<PlanDTO> plans = planDAO.getPlansByCondition(offset,size, search, userName, duration, sort, 1);

        // 결과값 출력
        System.out.println("Plans: " + plans.get(0).getTags());

        // then
        assertThat(plans).isNotNull();
        assertThat(plans).isNotEmpty();
        for (PlanDTO plan : plans) {
            assertThat(plan.getTitle()).contains(search);
            assertThat(plan.getWriters()).isNotEmpty();
            assertThat(plan.getTags()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("countPlansByCondition로 조건에 맞는 PlanDTO의 총 개수를 조회한다")
    void testCountPlansByCondition() {
        // given
        String search = ""; // 테스트 데이터베이스에 존재하는 제목 검색어
        String userName = ""; // 테스트 데이터베이스에 존재하는 사용자 이름
        int duration = 2; // 여행 기간

        // when
        int count = planDAO.countPlansByCondition(search, userName, duration);

        // 결과값 출력
        System.out.println("Count: " + count);

        // then
        assertThat(count).isGreaterThan(0);
    }
}
