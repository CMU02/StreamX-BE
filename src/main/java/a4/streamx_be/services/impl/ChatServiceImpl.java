package a4.streamx_be.services.impl;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV2;
import a4.streamx_be.services.ChatService;
import a4.streamx_be.util.TTSClient;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final OpenAiChatModel chatModel;
    private final TTSClient ttsClient;

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

    @Override
    public AIResDtoV2 generateResponseWithTTS(AIReqDtoV2 dto) {
        String aiText = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .user(dto.getMessage())
                .call()
                .content();

        String audioUrl = ttsClient.requestAudio(aiText);

        return new AIResDtoV2(aiText, audioUrl);
    }
}
