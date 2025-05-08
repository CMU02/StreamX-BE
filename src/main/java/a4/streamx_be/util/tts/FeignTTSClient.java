package a4.streamx_be.util.tts;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "tts-service", url = "http://${tts.api.url}")
public interface FeignTTSClient {
    @PostMapping()
    Map<String, String> synthesize(@RequestBody Map<String, String> requestBody);
}
