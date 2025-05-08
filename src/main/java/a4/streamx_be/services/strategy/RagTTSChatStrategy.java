package a4.streamx_be.services.strategy;

import a4.streamx_be.configuration.CharacterConfig;
import a4.streamx_be.domain.model.ChatType;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV3;
import a4.streamx_be.domain.model.Emotion;
import a4.streamx_be.services.ChatStrategy;
import a4.streamx_be.util.tts.FeignTTSService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;


/**
 * RAG + TTS 포함 채팅 전략 구현
 */
@Component
@RequiredArgsConstructor
public class RagTTSChatStrategy implements ChatStrategy<AIReqDtoV2, AIResDtoV3> {

    private final OpenAiChatModel chatModel;
    private final VectorStore vectorStore;
    private final CharacterConfig charConfig;
    private final ObjectMapper mapper;
    private final FeignTTSService feignTTSService;

    @Override
    public Boolean supports(ChatType type) {
        return type.equals(ChatType.RAG);
    }

    @Override
    public Flux<AIResDtoV3> execute(AIReqDtoV2 dto) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .similarityThreshold(0.75d)
                                .topK(4)
                                .build()
                ).promptTemplate(charConfig.getMiyoSystemTemplateV2())
                .build();

        return Mono.fromCallable(() -> {
            // 1. LLM 호출 (Blocking)
            Generation result = ChatClient.builder(chatModel).build()
                    .prompt()
                    .user(dto.message())
                    .advisors(advisor)
                    .call()
                    .chatResponse()
                    .getResult();

            // 2. Json 파싱 (Blocking)
            JsonNode root = mapper.readTree(result.getOutput().getText());
            String aiText = root.get("aiText").asText();
            Emotion emotion = mapper.treeToValue(root.get("emotion"), Emotion.class);
            String animation = root.get("animation").asText();

            // DownStream 에서 사용할 튜플로 묶기
            return Tuples.of(aiText, emotion, animation);
        })
            // 블로킹 작업은 boundedElastic 스케줄러에서 실행
            .subscribeOn(Schedulers.boundedElastic())
            // 3. Feign TTS Server 호출 (또 다른 블로킹)
            .flatMap(tuple ->
                Mono.fromCallable(() -> feignTTSService.getAudioUrl(tuple.getT1()))
                    .subscribeOn(Schedulers.boundedElastic())
                    // 4. 모든 결과를 모아서 DTO 생성
                    .map(audioUrl -> new AIResDtoV3(tuple.getT1(), tuple.getT2(), audioUrl, tuple.getT3()))
            ).flux(); // Mono -> Flux로 변환
    }
}
