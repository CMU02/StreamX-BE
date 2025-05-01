package a4.streamx_be.services.impl;

import a4.streamx_be.configuration.CharacterConfig;
import a4.streamx_be.services.ChatService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceTest.class);
    @Autowired
    ChatService chatService;

    @Autowired
    PromptTemplate getMiyoSystemTemplate;

    @Autowired
    VectorStore vectorStore;

    @Autowired
    OpenAiChatModel chatModel;

    @Test
    void generateResponseWithTTSV2() {

        String userResponse = "안녕 오늘 어때?";

        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.75d).topK(5).build())
                .promptTemplate(getMiyoSystemTemplate)
                .build();

        String aiText = ChatClient.builder(chatModel).build()
                .prompt()
                .user(userResponse)
                .advisors(qaAdvisor)
                .call()
                .content();

        log.info(aiText);
    }
}