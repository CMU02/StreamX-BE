package a4.streamx_be.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    /**
     * Redis에서 사용량을 기록하거나 조회할 때 사용할 Key를 생성합니다. <br />
     * 날짜는 항상 포함되며, 사용자 UID는 조건에 따라 포함하거나 와일드카드(*)로 대체됩니다. <br/>
     * 예시: <br/>
     * - includeUserId = true  → "usage:{userUid}:{yyyyMMdd}" <br/>
     * - includeUserId = false → "usage:*:{yyyyMMdd}" (모든 유저 대상 검색에 사용) <br/>
     *
     * @param userUid 사용자 UUID (사용자 ID를 키에 포함할 경우 사용됨)
     * @param includeUserUid true이면 userUid를 키에 포함하고, false이면 와일드카드(*)를 사용
     * @return Redis 키 문자열
     */
    public String makeRedisKey(UUID userUid, Boolean includeUserUid) {
        LocalDate utcToday = LocalDate.now(ZoneOffset.UTC);
        String yyyyMMdd = utcToday.format(DateTimeFormatter.BASIC_ISO_DATE);

        if (includeUserUid) {
            return String.format("%s:%s:%s", Prefix.USAGE.getValue(), userUid.toString(), yyyyMMdd);
        } else {
            return String.format("%s:*:%s", Prefix.USAGE.getValue(), yyyyMMdd);
        }
    }

    /**
     * Redis Hash에서 주어진 키(redisKey)와 필드(Field)에 해당하는 값을 가져와 Long 타입으로 반환합니다. <br/>
     * - 값이 존재하지 않거나, 형식이 잘못되어 파싱에 실패하면 0L을 반환합니다. <br/>
     * - 내부적으로 HashOperations를 사용하여 Redis Hash에서 데이터를 조회합니다. <br/>
     *
     * @param redisKey Redis에서 조회할 해시 키 (예: "usage:{userUid}:{yyyyMMdd}")
     * @param field 조회할 필드명 (예: Field.CHAT, Field.TTS 등)
     * @param hashOps Redis의 HashOperations 인스턴스 (opsForHash())
     * @return 해당 필드에 저장된 Long 값, 실패하거나 존재하지 않을 경우 0L
     */
    public Long getCurrentCount(String redisKey, Field field, HashOperations<String, Object, Object> hashOps) {
        switch (field) {
            case Field.CHAT -> {
                String currentChatStr = String.valueOf(hashOps.get(redisKey, Field.CHAT.getValue()));
                return parseLongOrZero(currentChatStr);
            }
            case Field.TTS -> {
                String currentTtsStr = String.valueOf(hashOps.get(redisKey, Field.TTS.getValue()));
                return parseLongOrZero(currentTtsStr);
            }
        }

        return 0L;
    }

    /**
     * 문자열을 Long 타입으로 안전하게 변환합니다. <br/>
     * - null 또는 공백("")이면 0L을 반환합니다. <br/>
     * - 변환 도중 예외(NumberFormatException 등)가 발생해도 0L을 반환합니다. <br/>
     *
     * @param str 변환할 문자열
     * @return Long 값으로 변환된 숫자 또는 0L (변환 불가 시)
     */
    private Long parseLongOrZero(String str) {
        try {
            return StringUtils.hasText(str) ? Long.parseLong(str) : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}
