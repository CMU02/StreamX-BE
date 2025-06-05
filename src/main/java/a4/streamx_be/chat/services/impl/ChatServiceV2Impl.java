package a4.streamx_be.chat.services.impl;

import a4.streamx_be.chat.domain.model.ChatType;
import a4.streamx_be.chat.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV3;
import a4.streamx_be.chat.services.ChatService;
import a4.streamx_be.chat.services.ChatStrategy;
import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceV2Impl implements ChatService<AIReqDtoV2, AIResDtoV3> {

    private final List<ChatStrategy<AIReqDtoV2, AIResDtoV3>> strategies;
    private final UsageService usageService;

    @Override
    public Flux<AIResDtoV3> chat(AIReqDtoV2 dto, User user) {
        usageService.recordChatUsage(user);
        usageService.recordTtsUsage(user);

        return strategies.stream()
                .filter(s -> s.supports(ChatType.RAG_TTS))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.RAG_TTS_NOT_SUPPORT))
                .execute(dto);
    }
}
