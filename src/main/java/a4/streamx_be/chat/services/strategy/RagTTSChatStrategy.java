package a4.streamx_be.chat.services.strategy;

import a4.streamx_be.chat.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV3;
import a4.streamx_be.chat.domain.model.ChatType;
import a4.streamx_be.chat.services.ChatStrategy;
import a4.streamx_be.util.RagChatProcessor;
import a4.streamx_be.util.tts.FeignTTSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * RAG_TTS + TTS 포함 채팅 전략 구현
 */
@Component
@RequiredArgsConstructor
public class RagTTSChatStrategy implements ChatStrategy<AIReqDtoV2, AIResDtoV3> {

    private final FeignTTSService feignTTSService;
    private final RagChatProcessor processor;

    @Override
    public Boolean supports(ChatType type) {
        return type.equals(ChatType.RAG_TTS);
    }

    @Override
    public Flux<AIResDtoV3> execute(AIReqDtoV2 dto) {
        return Mono.fromCallable(() -> processor.processRagChat(dto.message()))
            // 블로킹 작업은 boundedElastic 스케줄러에서 실행
            .subscribeOn(Schedulers.boundedElastic())
            // 3. Feign TTS Server 호출 (또 다른 블로킹)
            .flatMap(tuple ->
                Mono.fromCallable(() -> feignTTSService.getAudioUrl(tuple.getT1()))
                    .subscribeOn(Schedulers.boundedElastic())
                    // 4. 모든 결과를 모아서 DTO 생성
                    .map(audioUrl -> AIResDtoV3.builder()
                            .aiText(tuple.getT1())
                            .emotion(tuple.getT2())
                            .animation(tuple.getT3())
                            .audioUrl(audioUrl)
                            .build()
                    )
            ).flux(); // Mono -> Flux로 변환
    }
}
