package a4.streamx_be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 상황에 따라 에러 추가 가능
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // TTS 관련 에러
    TTS_AUDIO_URL_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "TTS 서버에서 AUDIO_URL 응답하지 않았습니다.");

    private final HttpStatus status;
    private final String message;
}
