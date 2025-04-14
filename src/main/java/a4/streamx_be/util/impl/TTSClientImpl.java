package a4.streamx_be.util.impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.util.TTSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TTSClientImpl implements TTSClient {

    private final RestTemplate restTemplate;

    @Value("${tts.api.url}")
    private String ttsApiUrl;

    @Override
    public String requestAudio(String text) {
        // 1. 요청 바디 생성
        Map<String, String> requestBody = Map.of("response", text);

        // 2. 헤더 설정 (Content Type = JSON)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 구성 (헤더 + 바디)
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // 4. 요청 및 응답 처리
        ResponseEntity<Map> response = restTemplate.postForEntity("http://" + ttsApiUrl, request, Map.class);

        // 5. 응답에서 audioUrl 추출
        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("audioUrl")) {
            return responseBody.get("audioUrl").toString();
        }

        // 응답 없을 시 예외 발생
        throw new NotFoundException(ErrorCode.TTS_AUDIO_URL_NOT_FOUND);
    }
}
