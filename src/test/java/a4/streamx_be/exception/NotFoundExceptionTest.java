package a4.streamx_be.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void notFoundException() {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        NotFoundException exception = new NotFoundException(errorCode);

        assertEquals("잘못된 요청입니다.", exception.getMessage());
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

}