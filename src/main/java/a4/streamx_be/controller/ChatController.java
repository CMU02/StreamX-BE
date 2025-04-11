package a4.streamx_be.controller;


import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.request.AIReqDtoV2;
import a4.streamx_be.domain.dto.response.AIResDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV2;
import a4.streamx_be.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/ai/message")
    public ResponseEntity<AIResDtoV1> generationV1(@RequestBody AIReqDtoV1 dto) {
        AIResDtoV1 result = chatService.generateChatV1(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/ai/message-audio")
    public ResponseEntity<AIResDtoV2> generationV2(@RequestBody AIReqDtoV2 dto) {
        AIResDtoV2 result = chatService.generateResponseWithTTS(dto);
        return ResponseEntity.ok(result);
    }
}
