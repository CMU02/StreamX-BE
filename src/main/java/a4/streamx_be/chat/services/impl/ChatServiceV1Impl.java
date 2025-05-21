package a4.streamx_be.chat.services.impl;

import a4.streamx_be.chat.domain.model.ChatType;
import a4.streamx_be.chat.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV1;
import a4.streamx_be.chat.services.ChatService;
import a4.streamx_be.chat.services.ChatStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceV1Impl implements ChatService<AIReqDtoV1, AIResDtoV1> {

    private final List<ChatStrategy<AIReqDtoV1, AIResDtoV1>> strategies;

    @Override
    public Flux<AIResDtoV1> chat(AIReqDtoV1 dto) {
        return strategies.stream()
                .filter(s -> s.supports(ChatType.PLAIN))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Support Plain Chat Strategy"))
                .execute(dto);
    }
}