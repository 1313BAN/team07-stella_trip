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

    public boolean acquirePlanLock(int planId, Integer userId, long timeout) {
        String lockKey = LOCK_PREFIX + planId;
        Integer currentUserId = (Integer) redisTemplate.opsForValue().get(lockKey);

        // 만일 같은 사용자가 락을 획득한 경우, 만료 시간을 연장
        if (userId.equals(currentUserId)) {
            redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
            return true;
        }

        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, userId, timeout, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public boolean releasePlanLock(int planId, Integer userId) {
        String key = LOCK_PREFIX + planId;
        Integer owner = (Integer) redisTemplate.opsForValue().get(key);
        if (!userId.equals(owner)) {
            return false;   // 소유자 불일치
        }
        return redisTemplate.opsForValue().getAndDelete(key) != null;
    }

    public Integer checkPlanLock(int planId) {
        return (Integer) redisTemplate.opsForValue().get(LOCK_PREFIX + planId);
    }
}