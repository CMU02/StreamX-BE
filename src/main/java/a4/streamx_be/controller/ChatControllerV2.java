package a4.streamx_be.controller;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV3;
import a4.streamx_be.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatControllerV2 {
    private final ChatService<AIReqDtoV1, AIResDtoV1> plainChatService;
    private final ChatService<AIReqDtoV2, AIResDtoV3> ragChatService;

    @PostMapping("/ai/message")
    public Mono<AIResDtoV1> chatV1(@RequestBody AIReqDtoV1 dto) {
        return plainChatService.chat(dto)
                .next(); // Mono 변환
    }

    @PostMapping("/ai/message-audio")
    public Flux<AIResDtoV3> chatWithTTSV2(@RequestBody AIReqDtoV2 dto) {
        return ragChatService.chat(dto);
    }
}
