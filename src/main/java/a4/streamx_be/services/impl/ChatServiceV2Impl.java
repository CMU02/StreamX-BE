package a4.streamx_be.services.impl;

import a4.streamx_be.domain.dto.ChatType;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV3;
import a4.streamx_be.services.ChatServiceV2;
import a4.streamx_be.services.ChatStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceV2Impl implements ChatServiceV2 {

    private final List<ChatStrategy<AIReqDtoV2, AIResDtoV3>> strategies;

    @Override
    public Flux<AIResDtoV3> chat(AIReqDtoV2 dto) {
        return strategies.stream()
                .filter(s -> s.supports(ChatType.RAG))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Support V2 Chat Strategy"))
                .execute(dto);
    }
}
