package a4.streamx_be.user.service.Impl;

import a4.streamx_be.user.domain.dto.response.UsageResponse;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.entity.UserUsage;
import a4.streamx_be.user.repository.UsageRepository;
import a4.streamx_be.user.repository.UserRepository;
import a4.streamx_be.user.service.UsageQueryService;
import a4.streamx_be.util.Field;
import a4.streamx_be.util.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsageQueryServiceImpl implements UsageQueryService {
    private final RedisTemplate<String, String> defaultRedisTemplate;
    private final UsageRepository usageRepository;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Override
    public UsageResponse getUsage(UUID userUid) {
        return null;
    }

//    @Scheduled(cron = "*/30 * * * * *", zone = "UTC") // 테스트용 30초마다
    @Scheduled(cron = "0 0 1 * * *", zone = "UTC") // 매일 새벽 1시 집계
    public void syncDailyUsageFromRedisToMySql() {
        // example: usage:*:20250604
        String redisKey = redisUtil.makeRedisKey(null, false);
        Set<String> keys = defaultRedisTemplate.keys(redisKey);

        if (keys.isEmpty()) {
            log.info("[UsageSync] 오늘 Redis에 기록된 사용량이 없습니다.");
            return;
        }

        HashOperations<String, Object, Object> hashOps = defaultRedisTemplate.opsForHash();
        Integer successCount = 0; // 성공 횟수

        for (String key : keys) {
            try {
                String[] parts = key.split(":");
                if (parts.length != 3) {
                    log.warn("[UsageSync] 잘못된 Redis키 형식, 건너뜀: {}", key);
                    return;
                }

                // 두번째 부분을 UUID 파싱
                UUID userUid = UUID.fromString(parts[1]);
                // Redis 해시에서 chatCount, ttsCount 꺼내기 (null Check 포함)
                Long chatCount = redisUtil.getCurrentCount(key, Field.CHAT, hashOps);
                Long ttsCount = redisUtil.getCurrentCount(key, Field.TTS, hashOps);

                // User Entity 조회 (findById가 기본 제공 메서드)
                Optional<User> userOpt = userRepository.findById(userUid);

                if (userOpt.isEmpty()) {
                    log.warn("[UsageSync] 해당 사용자 없음, 건너뜀: uid={}", userUid);
                    return;
                }

                userOpt.ifPresent(user -> {
                    LocalDate usageDate = LocalDate.parse(parts[2], DateTimeFormatter.BASIC_ISO_DATE);
                    Instant usageTime = usageDate.atStartOfDay(ZoneOffset.UTC).toInstant();

                    Optional<UserUsage> usageOpt = usageRepository.findByUserAndUsageTime(user, usageTime);

                    UserUsage usage = usageOpt.orElseGet(() -> UserUsage.builder()
                            .user(user)
                            .usageTime(usageTime)
                            .chatCount(0L)
                            .ttsCount(0L)
                            .build());

                    // Redis에 읽어온 값을 기존 값에서 증감
                    usage.changeChatCount(chatCount);
                    usage.changeTtsCount(ttsCount);

                    // MySQL 저장
                    usageRepository.save(usage);

                    // MySQL 저장이 정상 완료 되었으니 Redis 키 삭제
                    defaultRedisTemplate.delete(key);
                });

                successCount++;
            } catch (Exception e) {
                log.error("[UsageSync] Key 처리 실패 : {} ", key, e);
            }
        }

        log.info("[UsageSync] 총 {}건의 사용량을 MySQL에 동기화했으며, Redis에서 해당 키들을 삭제했습니다.", successCount);
    }
}
