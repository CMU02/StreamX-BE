package a4.streamx_be.chat.repository;

import a4.streamx_be.util.Prefix;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatMemoryRepository implements ChatMemoryRepository {

    private final RedisTemplate<String, String> defaultRedisTemplate;
    private final ObjectMapper customMapper;


    @Override
    public List<String> findConversationIds() {
        // 모든 chat:memory:* key를 조회해서 마지막 부분 conversationId만 추출
        Set<String> keys = defaultRedisTemplate.keys(Prefix.CHAT_MEMORY.getValue() + ":");
        if (keys.isEmpty()) {
            return List.of();
        }

        return keys.stream()
                .map(key -> key.substring(Prefix.CHAT_MEMORY.getValue().length()))
                .toList();
     }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        String redisKey = Prefix.CHAT_MEMORY.getValue() + conversationId;
        // Redis List 전체 범위 조회
        List<String> jsonList = defaultRedisTemplate.opsForList().range(redisKey, 0, -1);
        if (jsonList == null) {
            return List.of();
        }

        return jsonList.stream()
                .map(this::deserializeMessage)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        String redisKey = Prefix.CHAT_MEMORY.getValue() + conversationId;

        List<String> jsonList = messages.stream()
                .map(this::serializeMessage)
                .filter(Objects::nonNull)
                .toList();


        if (!jsonList.isEmpty()) {
            // 순서를 보존하며 List에 일괄 삽입
            defaultRedisTemplate.opsForList()
                    .rightPushAll(redisKey, jsonList);
        }
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        String redisKey = Prefix.CHAT_MEMORY.getValue() + conversationId;
        defaultRedisTemplate.delete(redisKey);
    }

    private Message deserializeMessage(String json) {
        try {
            JsonNode node = customMapper.readTree(json);
            String messageType = node.get("messageType").asText();

            MessageType type = MessageType.fromValue(messageType);
            String content = node.get("content").asText();

            return new Message() {
                @Override
                public MessageType getMessageType() {
                    return type;
                }

                @Override
                public String getText() {
                    return content;
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return null;
                }
            };
        } catch (Exception e) {
            log.warn("Message deserialization error: {}", e.getMessage());
            return null;
        }
    }

    private String serializeMessage(Message message) {
        try {

            ObjectNode node = customMapper.createObjectNode();
            node.put("messageType", message.getMessageType().getValue());
            node.put("content", message.getText());

            return customMapper.writeValueAsString(node);
        } catch (Exception e) {
            log.warn("Message serialization error: {}", e.getMessage());
            return null;
        }
    }
}
