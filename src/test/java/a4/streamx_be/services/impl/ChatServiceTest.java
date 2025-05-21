package a4.streamx_be.services.impl;

import a4.streamx_be.configuration.CharacterConfig;
import a4.streamx_be.chat.domain.dto.response.AIResDtoV3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceTest.class);
    @Autowired
    CharacterConfig characterConfig;

    @Autowired
    VectorStore vectorStore;

    @Autowired
    OpenAiChatModel chatModel;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void generateResponseWithTTSV2() {

        String userResponse = "안녕 오늘 어때?";

        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.75d).topK(5).build())
                .promptTemplate(characterConfig.getMiyoSystemTemplate())
                .build();

        String aiText = ChatClient.builder(chatModel).build()
                .prompt()
                .user(userResponse)
                .advisors(qaAdvisor)
                .call()
                .content();

        log.info(aiText);
    }

    @Test
    @DisplayName(value = "Show Miyo System template v2")
    void ShowSystemTemplateV2() {
        log.info(characterConfig.getMiyoSystemTemplateV2().getTemplate());
    }

    @Test
    @DisplayName(value = "Test Miyo System template_V2 RAG API Logic")
    void TestSystemTemplateV2_API() throws JsonProcessingException {
        // given
        String userRequest = "오늘 보드게임 어땠어?";
        // when
        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.75d).topK(4).build())
                .promptTemplate(characterConfig.getMiyoSystemTemplateV2())
                .build();

        Generation result = ChatClient.builder(chatModel).build()
                .prompt()
                .user(userRequest)
                .advisors(qaAdvisor)
                .call()
                .chatResponse()
                .getResult();

        // then
        String text = result.getOutput().getText();
        AIResDtoV3 dto = mapper.readValue(text, AIResDtoV3.class);

        log.info(text);

        log.info(dto.aiText());
        log.info(dto.audioUrl());
        log.info(dto.animation());
        log.info(String.valueOf(dto.emotion()));
    }
    
}