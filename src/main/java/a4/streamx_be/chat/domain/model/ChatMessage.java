package a4.streamx_be.chat.domain.model;

public record ChatMessage(
        String messageType, String content
) {
}
