package a4.streamx_be.user.service;

import a4.streamx_be.exception.UsageLimitExceededException;
import a4.streamx_be.user.domain.entity.User;

/**
 * Chat API를 호출전 "한도를 검증"하고,
 * Chat API를 호출 후 "사용량을 증가" 시키는 기능을 수행
 */
public interface UsageService {
    /**
     * 사용자가 "텍스트 채팅만" 호출했을 때 호출.
     * @param user 사용자를 식별하기 위한 객체
     * @throws UsageLimitExceededException 채팅 한도 초과 시
     */
    void recordChatUsage(User user) throws UsageLimitExceededException;

    /**
     * 사용자가 "TTS 포함 채팅" 호출 했을 때 호출
     * @param user 사용자를 식별하기 위한 객체
     * @throws UsageLimitExceededException 채팅 혹은 TTS 한도 초과 시
     */
    void recordTtsUsage(User user) throws UsageLimitExceededException;
}
