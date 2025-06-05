package a4.streamx_be.user.service.Impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.UsageLimitExceededException;
import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.service.UsageService;
import a4.streamx_be.util.Field;
import a4.streamx_be.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * UsageService 구현체
 */
@Service
@RequiredArgsConstructor
public class RecordUsageService implements UsageService {

    private final RedisTemplate<String, String> defaultRedisTemplate;
    private final RedisUtil redisUtil;

    @Override
    public void recordChatUsage(User user) throws UsageLimitExceededException {
        MemberShip memberShip = user.getMemberShip();
        HashOperations<String, Object, Object> hashOps = defaultRedisTemplate.opsForHash();

        String redisKey = redisUtil.makeRedisKey(user.getUid(), true);
        Long weeklyChatLimit = memberShip.getWeeklyChatLimit();// -1 일 경우 무제한
        Long currentChatCount = redisUtil.getCurrentCount(redisKey, Field.CHAT, hashOps);

        checkWeeklyLimit(weeklyChatLimit, currentChatCount, Field.CHAT);
        incrementCountRedis(redisKey, Field.CHAT, currentChatCount);
    }

    @Override
    public void recordTtsUsage(User user) throws UsageLimitExceededException {
        MemberShip memberShip = user.getMemberShip();
        HashOperations<String, Object, Object> hashOps = defaultRedisTemplate.opsForHash();

        String redisKey = redisUtil.makeRedisKey(user.getUid(), true);
        Long weeklyTtsLimit = memberShip.getWeeklyTtsLimit();
        Long currentTTSCount = redisUtil.getCurrentCount(redisKey, Field.TTS, hashOps);

        checkWeeklyLimit(weeklyTtsLimit, currentTTSCount, Field.TTS);
        incrementCountRedis(redisKey, Field.TTS, currentTTSCount);
    }

    /**
     * "지금"부터 "다음 UTC 자정"까지 남은 초(second) 계산
     *  TTL로 설정하여, 해당 일자가 끝나는 순간 만료됨
     */
    private Long getSecondsUntilNextYUtcMidnight() {
        Instant now = Instant.now();
        LocalDate utcTomorrow = LocalDate.now().plusDays(1);
        Instant utcTomorrowMidnight = utcTomorrow.atStartOfDay(ZoneOffset.UTC).toInstant();
        return Duration.between(now, utcTomorrowMidnight).getSeconds();
    }


    /**
     * 주간 사용 한도를 초과했는지 확인하는 메서드입니다. <br /><br/>
     * - 주간 한도({@code weeklyLimit})가 설정되어 있고, 현재 사용량({@code currentCount})이 0 이상일 때 체크 수행함 <br/>
     * - 사용량이 한도를 초과하면 {@link UsageLimitExceededException} 발생 시킵니다. <br />
     * - 필드명이 {@code FIELD_CHAT}일 경우는 채팅 한도 초과, {@code FIELD_TTS}일 경우는 TTS 한도초과 간주합니다.
     * @param weeklyLimit 사용자의 주간 사용 한도 (-1이면 무제한)
     * @param currentCount 현재까지의 사용량 (0 이상)
     * @param field {@code FIELD_CHAT} or {@code FIELD_TTS}
     * @throws UsageLimitExceededException 사용량이 주간 한도를 초과한 경우 예외 발생
     */
    private void checkWeeklyLimit(Long weeklyLimit, Long currentCount, Field field) {
        if (weeklyLimit != -1 && currentCount >= 0) {
            if (currentCount >= weeklyLimit) {
                String countRemaining = String.valueOf(weeklyLimit - currentCount); // 남은 횟수
                if (field.equals(Field.CHAT)) {
                    throw new UsageLimitExceededException(ErrorCode.EXCEEDED_USAGE_CHAT_LIMIT, "남은 횟수:" + countRemaining);
                } else {
                    throw new UsageLimitExceededException(ErrorCode.EXCEEDED_USAGE_TTS_LIMIT, "남은 횟수: " + countRemaining);
                }
            }
        }
    }

    /**
     * Redis Hash에 사용량 카운트를 증가시키는 메서드 입니다. <br/><br/>
     * - 지정된 {@code redisKey}와 {@code field} 조합으로 Redis Hash에 카운트를 기록합니다. <br />
     * - 해당 필드가 없으면 새로운 값을 넣고, 해당 키의 만료 시간을 설정합니다. <br>
     * - 이미 존재하는 필드라면 값을 1 증가시킵니다. <br/>
     * @param redisKey Redis Hash 키 (예: "usage:{userUid}:{yyyyMMdd}")
     * @param field 증가시킬 필드명 (예: {@code FIELD_CHAT} 또는 {@code FIELD_TTS})
     * @param currentCount 현재까지의 사용량 (필드가 없을 경우 초기값으로 사용됨)
     */
    private void incrementCountRedis(String redisKey, Field field, Long currentCount) {
        Boolean hasField = defaultRedisTemplate.opsForHash().hasKey(redisKey, field.getValue());
        if (!hasField) {
            defaultRedisTemplate.opsForHash().put(redisKey, field.getValue(), String.valueOf(currentCount + 1));

            Long secondsToMidnight = getSecondsUntilNextYUtcMidnight();
            defaultRedisTemplate.expire(redisKey, secondsToMidnight, TimeUnit.SECONDS);
        } else {
            defaultRedisTemplate.opsForHash().increment(redisKey, field.getValue(), 1L);
        }
    }
}
