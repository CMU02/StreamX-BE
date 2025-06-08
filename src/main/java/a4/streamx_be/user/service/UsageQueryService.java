package a4.streamx_be.user.service;

import a4.streamx_be.user.domain.dto.response.UsageResponse;
import a4.streamx_be.user.domain.entity.User;

public interface UsageQueryService {
    /**
     * 사용자의 주간 사용량을 조회합니다.
     * @param user 사용자 정보
     * @return 사용량의 정보
     */
    UsageResponse getUsage(User user);
}
