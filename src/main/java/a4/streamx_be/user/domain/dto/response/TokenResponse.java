package a4.streamx_be.user.domain.dto.response;

public record TokenResponse(
        String token
) {
    public TokenResponse(String token) {
        this.token = token;
    }
}
