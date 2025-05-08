package a4.streamx_be.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class AnimationConfig {
    private List<String> allAnimations;

    @Builder
    public AnimationConfig() throws Exception {
//        1. classpath 리로스로부터 파일 열기
        ClassPathResource resource = new ClassPathResource("fullAnimation.json");
        try (InputStream is = resource.getInputStream()) {
//            2. Jackson으로 Json 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);
            JsonNode array = root.get("allAnimations");

//            3. List<String> 변환
            this.allAnimations = StreamSupport.stream(array.spliterator(), false)
                    .map(JsonNode::asText)
                    .toList();
        }
    }

    @Builder
    public List<String> getAllAnimations() {
        return this.allAnimations.stream()
                .map(animation -> "\t- " + animation + "\n")
                .toList();
    }
}
