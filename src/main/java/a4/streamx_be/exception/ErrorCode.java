package a4.streamx_be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // === Chat API Error Code ===
    PLAN_RAG_NOT_SUPPORT(BAD_REQUEST, "Not Support Plain Chat Strategy"),
    RAG_TTS_NOT_SUPPORT(BAD_REQUEST, "Not Support RAG Chat Strategy"),
    /**
     * TTS 서버가 연결되지 않습니다.
     */
    TTS_AUDIO_URL_NOT_FOUND(INTERNAL_SERVER_ERROR, "TTS Server Not Found"),
    /**
     * TTS 서버 연결 오류
     */
    TTS_SERVER_CONNECTION_ERROR(INTERNAL_SERVER_ERROR, "TTS Server Connection Error"),

    // === Common ===
    /**
     * 서버 내부 오류가 발생했습니다.
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "Internal Server Error"),
    /**
     * 요청한 리소스를 찾을 수 없습니다.
     */
    ENTITY_NOT_FOUND(INTERNAL_SERVER_ERROR, "Entity Not Found"),

    // === Auth API Error Code ===
    /**
     * 소셜 로그인에 실패했습니다.
     */
    OAUTH2_LOGIN_FAILED(UNAUTHORIZED, "OAuth2 Login Failed"),
    /**
     * 이미 존재하는 이메일 입니다.
     */
    EMAIL_ALREADY_EXISTS(CONFLICT, "Email Already Exists"),
    /**
     * 사용자를 찾을 수 없습니다.
     */
    USER_NOT_FOUND(UNAUTHORIZED, "User Not Found"),
    /**
     * 비밀번호가 일치하지 않습니다.
     */
    PASSWORD_MISMATCH(UNAUTHORIZED, "Password Mismatch"),
    /**
     * 유효하지 않는 토큰입니다.
     */
    INVALID_TOKEN(UNAUTHORIZED, "Invalid Token"),
    /**
     * 만료된 토근 입니다.
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "Expired Token"),
    /**
     * 인증이 필요한 요청입니다.
     */
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "Unauthorized Request"),
    /**
     * 소셜 로그인 정보가 누락되었습니다.
     */
    MISSING_OAUTH2_ATTRIBUTE(UNAUTHORIZED, "Missing OAuth2 Attribute"),
    /**
     * 인증되지 않은 사용자입니다.
     */
    NOT_AUTHENTICATED_USER(UNAUTHORIZED, "Not Authenticated User"),

    // === SignUp Error Code ===
    /**
     * 이메일 형식이 올바르지 않습니다.
     */
    INVALID_EMAIL_FORMAT(BAD_REQUEST, "Invalid Email Format"),
    /**
     * 비밀번호 최소 8자 이상이어야 하며, 특수문자를 포함해야 합니다.
     */
    WEAK_PASSWORD(BAD_REQUEST, "Weak Password"),
    /**
     * 필수 입력 항목이 누락되었습니다.
     */
    REQUIRED_FIELD_MISSING(BAD_REQUEST, "A Required Field Is Missing");


    private final HttpStatus status;
    private final String message;
}
