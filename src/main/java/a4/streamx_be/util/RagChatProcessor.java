package a4.streamx_be.util;

import a4.streamx_be.chat.domain.model.Emotion;
import a4.streamx_be.chat.repository.RedisChatMemoryRepository;
import a4.streamx_be.configuration.CharacterConfig;
import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.JsonErrorException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RagChatProcessor {
    private final OpenAiChatModel chatModel;
    private final VectorStore vectorStore;
    private final CharacterConfig charConfig;
    private final ObjectMapper mapper;

    public Tuple3<String, Emotion, String> processRagChat(String message) {
        Generation generated = ChatClient.builder(chatModel).build()
                .prompt()
                .user(message)
                .advisors(RagAdvisor())
                .call()
                .chatResponse()
                .getResult();

        try {
            return parseChatResult(generated.getOutput().getText());
        } catch (JsonMappingException e) {
            throw new JsonErrorException(ErrorCode.SERVER_ERROR);
        } catch (Exception e) {
            // 다른 예외 처리 (예: 로그 남기기)
            throw new RuntimeException("Unexpected error during chat processing", e);
        }
    }

    // VectorStore, SearchRequest, 프롬프트 템플릿 설정 및 Advisor 객체 리턴
    private QuestionAnswerAdvisor RagAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .similarityThreshold(0.75d)
                                .topK(4)
                                .build()
                ).promptTemplate(charConfig.getMiyoSystemTemplateV2())
                .build();
    }

    // JSON 파싱하여 Tuple로 반환
    private Tuple3<String, Emotion, String> parseChatResult(String json) throws Exception {
        JsonNode root = mapper.readTree(json);

        String aiText = root.get("aiText").asText();
        Emotion emotion = mapper.treeToValue(root.get("emotion"), Emotion.class);
        String animation = root.get("animation").asText();

        return Tuples.of(aiText, emotion, animation);
    }
}
