package a4.streamx_be.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        int code,
        String message,
        LocalDateTime timestamp
) { }
