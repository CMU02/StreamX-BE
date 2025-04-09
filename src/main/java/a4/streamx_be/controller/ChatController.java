package a4.streamx_be.controller;


import a4.streamx_be.domain.dto.request.AIReqDto;
import a4.streamx_be.domain.dto.response.AIResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiChatModel chatModel;
    private final PineconeVectorStore vectorStore;


    @PostMapping("/ai/message")
    public AIResDto generation(@RequestBody AIReqDto dto) {
        String content = ChatClient.builder(chatModel)
                .build()
                .prompt(dto.getPrompt())
                .advisors(
//                        RAG는 잠시 주석
//                        new QuestionAnswerAdvisor(vectorStore)
                )
                .user(dto.getMessage())
                .call()
                .content();

        return new AIResDto(content);
    }
}
