package com.ssafy.stella_trip.plan.scheduler;

import com.ssafy.stella_trip.dao.plan.PlanDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagConutScheduler {
    // 매일 자정에 실행
    private final PlanDAO planDAO;

    @Scheduled(cron = "0 0 * * * *")
    public void tagCountSchedule() {
        planDAO.updateTagCount();
    }
}
