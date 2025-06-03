package a4.streamx_be.exception;

public class UsageLimitExceededException extends BaseException {
    private final ErrorCode errorCode;
    private String message;

    public UsageLimitExceededException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UsageLimitExceededException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
