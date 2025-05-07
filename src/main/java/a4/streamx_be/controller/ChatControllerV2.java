package a4.streamx_be.controller;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV3;
import a4.streamx_be.services.ChatServiceV1;
import a4.streamx_be.services.ChatServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatControllerV2 {
    private final ChatServiceV1 chatServiceV1;
    private final ChatServiceV2 chatServiceV2;

    @PostMapping("/ai/message")
    public Mono<AIResDtoV1> chatV1(@RequestBody AIReqDtoV1 dto) {
        return chatServiceV1.chat(dto)
                .next(); // Mono 변환
    }

    @PostMapping("/ai/message-audio")
    public Flux<AIResDtoV3> chatWithTTSV2(@RequestBody AIReqDtoV2 dto) {
        return chatServiceV2.chat(dto);
    }
}
