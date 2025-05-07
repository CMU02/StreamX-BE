package a4.streamx_be.services.impl;

import a4.streamx_be.domain.dto.ChatType;
import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.services.ChatStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 일반 채팅 전략
 */
@Component
@RequiredArgsConstructor
public class PlainChatStrategy implements ChatStrategy<AIReqDtoV1, AIResDtoV1> {

    private final OpenAiChatModel chatModel;

    @Override
    public Boolean supports(ChatType type) {
        return type.equals(ChatType.PLAIN);
    }

    @Override
    public Flux<AIResDtoV1> execute(AIReqDtoV1 dto) {
        return Mono.fromCallable(() -> {
            String aiText = ChatClient.builder(chatModel).build()
                    .prompt(dto.prompt())
                    .user(dto.message())
                    .call()
                    .content();

            return new AIResDtoV1(aiText);
        })
        /*
          Reactor에서 블로킹 호출 (ex: 외부 API 호출, 파일 I/O, DB 쿼리)을 안전하게
          처리하기 위해 "별도의 스레드 풀"에서 실행하도록 지정해주는 역할
         */
        .subscribeOn(Schedulers.boundedElastic())
        .flux();
    }
}
