package a4.streamx_be.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {
    CHAT("chatCount"), TTS("ttsCount");

    private final String value;
}
