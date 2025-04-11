package a4.streamx_be.services.impl;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.services.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final OpenAiChatModel chatModel;
    private final PineconeVectorStore vectorStore;

    @Override
    public AIResDtoV1 generateChatV1(AIReqDtoV1 dto) {
        String response = ChatClient.builder(chatModel)
                .build()
                .prompt(dto.getPrompt())
                .user(dto.getMessage())
                .call()
                .content();

        return new AIResDtoV1(response);
    }
}
