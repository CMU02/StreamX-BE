package a4.streamx_be.domain.dto.request;

import lombok.Getter;

@Getter
public class AIReqDtoV2 {
    private String prompt; // Unity에서 받은 프롬프트
    private String message; // Unity에서 받은 사용자 메시지
}
