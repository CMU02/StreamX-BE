package a4.streamx_be.chat.domain.dto.request;

import lombok.Data;

// Unity에서 받은 사용자 메시지
public record AIReqDtoV2(String message) { }
