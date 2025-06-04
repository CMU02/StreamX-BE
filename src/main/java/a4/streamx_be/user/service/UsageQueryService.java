package a4.streamx_be.user.service;

import a4.streamx_be.user.domain.dto.response.UsageResponse;

import java.util.UUID;

public interface UsageQueryService {
    /**
     * 사용자의 주간 사용량을 조회합니다.
     * @param userUid 사용자 고유 아이디
     * @return 사용량의 정보
     */
    UsageResponse getUsage(UUID userUid);
}
