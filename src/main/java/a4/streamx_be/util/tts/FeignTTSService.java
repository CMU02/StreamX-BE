package a4.streamx_be.util.tts;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeignTTSService {
    private final FeignTTSClient feignTTSClient;

    /**
     * 주어진 텍스트로 TTS 서버로부터 TTS 오디어 URL을 받아 옵니다.
     */
    public String getAudioUrl(String text) {
        Map<String, String> requestBody = Map.of("response", text);
        Map<String, String> response = feignTTSClient.synthesize(requestBody);

        String audioUrl = response.get("audioUrl");
        if (audioUrl == null || audioUrl.isBlank()) {
            throw new NotFoundException(ErrorCode.TTS_AUDIO_URL_NOT_FOUND);
        }

        return audioUrl;
    }
}
