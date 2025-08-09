package a4.streamx_be.util.tts;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "tts-service", url = "${tts.api.url}")
public interface FeignTTSClient {
    @PostMapping("/tts")
    Map<String, Object> synthesize(@RequestBody Map<String, String> requestBody);
}
