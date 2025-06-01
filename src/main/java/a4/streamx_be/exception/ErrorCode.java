package a4.streamx_be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    PLAN_RAG_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "Not Support Plain Chat Strategy"),
    RAG_TTS_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "Not Support RAG Chat Strategy"),

    TTS_AUDIO_URL_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "TTS Server Not Found"),
    TTS_SERVER_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TTS Server Connection Error"),

    JSON_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON Mapping Error");

    private final HttpStatus status;
    private final String message;
}
