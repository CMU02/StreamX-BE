package a4.streamx_be.user.domain.dto.response;

public record UsageResponse(
        Long weeklyChatUsed,
        Long weeklyTtsUsed,
        Long weeklyChatRemaining,
        Long weeklyTtsRemaining
) {
    public UsageResponse {
    }
}
