package a4.streamx_be.exception;

/**
 * 추상 부모 예외 클래스
 */
public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public abstract ErrorCode getErrorCode();
}
