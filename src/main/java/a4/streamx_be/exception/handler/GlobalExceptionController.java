package a4.streamx_be.exception.handler;

import a4.streamx_be.domain.dto.ErrorResponse;
import a4.streamx_be.exception.BaseException;
import a4.streamx_be.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionController {

    /**
     * 400번대 Base 예외 컨트롤러
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
