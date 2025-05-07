package a4.streamx_be.services;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import reactor.core.publisher.Flux;

public interface ChatServiceV1 {
    Flux<AIResDtoV1> chat(AIReqDtoV1 dto);
}
