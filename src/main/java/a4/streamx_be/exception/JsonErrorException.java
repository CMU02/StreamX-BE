package a4.streamx_be.exception;

public class JsonErrorException extends BaseException {
    private final ErrorCode errorCode;

    public JsonErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
