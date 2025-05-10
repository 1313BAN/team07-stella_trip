package com.ssafy.stella_trip.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PlanLockUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String LOCK_PREFIX = "plan-lock:";

    /**
     * Redis 분산 락을 획득합니다.
     * @param planId  락을 획득할 여행 계획 ID
     * @param timeout 락의 만료 시간 (초)
     * @return 락 획득 성공 여부
     */
    public boolean acquirePlanLock(int planId, Integer userId, long timeout) {
        String lockKey = LOCK_PREFIX + planId;
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, userId, timeout, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public boolean releasePlanLock(int planId) {
        return redisTemplate.opsForValue().getAndDelete(LOCK_PREFIX + planId) != null;
    }

    public Integer checkPlanLock(int planId) {
        return (Integer) redisTemplate.opsForValue().get(LOCK_PREFIX + planId);
    }
}