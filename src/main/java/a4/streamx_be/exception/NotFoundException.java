package a4.streamx_be.exception;

/**
 * 예외 사용 방법
 * @apiNote throw new NotFoundException(ErrorCode.USER_NOT_FOUND)
 */
public class NotFoundException extends BaseException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
