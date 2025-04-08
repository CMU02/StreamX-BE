package a4.streamx_be.EnvironmentLoad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnvLoadTest {

    @Autowired
    private Environment env;

    @Test
    public void testPineconeKeyFromEnv() {
        String pineconeKey = env.getProperty("spring.ai.vectorstore.pinecone.api-key");
        String projectId = env.getProperty("spring.ai.vectorstore.pinecone.project-id");
        String environment = env.getProperty("spring.ai.vectorstore.pinecone.environment");
        String openAiKey = env.getProperty("spring.ai.openai.api-key");

        assertThat(pineconeKey).isNotNull();
        assertThat(projectId).isNotNull();
        assertThat(environment).isNotNull();
        assertThat(openAiKey).isNotNull();

        System.out.println("✅ Loaded Pinecone Key from Environment: " + pineconeKey);
        System.out.println("✅ Loaded Pinecone Project-ID from Environment:" + projectId);
        System.out.println("✅ Loaded Pinecone Environment from Environment:" + environment);
        System.out.println("✅ Loaded OpenAI Key from Environment:" + openAiKey);
    }
}
