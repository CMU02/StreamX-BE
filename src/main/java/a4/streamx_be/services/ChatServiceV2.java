package a4.streamx_be.services;

import a4.streamx_be.domain.dto.ChatType;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV3;
import reactor.core.publisher.Flux;

public interface ChatServiceV2 {
    Flux<AIResDtoV3> chat(AIReqDtoV2 dto);
}
