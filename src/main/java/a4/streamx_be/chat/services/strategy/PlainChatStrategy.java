package a4.streamx_be.chat.services.strategy;

import a4.streamx_be.chat.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV3;
import a4.streamx_be.chat.domain.model.ChatType;
import a4.streamx_be.chat.services.ChatStrategy;
import a4.streamx_be.util.RagChatProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 일반 채팅 + RAG 전략
 */
@Component
@RequiredArgsConstructor
public class PlainChatStrategy implements ChatStrategy<AIReqDtoV1, AIResDtoV3> {

    private final RagChatProcessor processor;

    @Override
    public Boolean supports(ChatType type) {
        return type.equals(ChatType.PLAIN_RAG);
    }

    @Override
    public Flux<AIResDtoV3> execute(AIReqDtoV1 dto) {
        return Mono.fromCallable(() -> processor.processRagChat(dto.message()))
        .map(tuple ->
                AIResDtoV3.builder()
                        .aiText(tuple.getT1())
                        .emotion(tuple.getT2())
                        .audioUrl(null)
                        .animation(tuple.getT3())
                        .build()
        ).flux();
    }
}
