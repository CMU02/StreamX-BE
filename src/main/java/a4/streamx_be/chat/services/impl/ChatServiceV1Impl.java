package a4.streamx_be.chat.services.impl;

import a4.streamx_be.chat.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV3;
import a4.streamx_be.chat.domain.model.ChatType;
import a4.streamx_be.chat.services.ChatService;
import a4.streamx_be.chat.services.ChatStrategy;
import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceV1Impl implements ChatService<AIReqDtoV1, AIResDtoV3> {

    private final List<ChatStrategy<AIReqDtoV1, AIResDtoV3>> strategies;

    @Override
    public Flux<AIResDtoV3> chat(AIReqDtoV1 dto) {
        return strategies.stream()
                .filter(s -> s.supports(ChatType.PLAIN_RAG))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLAN_RAG_NOT_SUPPORT))
                .execute(dto);
    }
}