package a4.streamx_be.exception;

/**
 * 예외 사용 방법
 * @apiNote throw new NotFoundException(ErrorCode.USER_NOT_FOUND)
 */
public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return null;
    }
}
