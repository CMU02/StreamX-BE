package a4.streamx_be.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Prefix {
    USAGE("usage"),
    CHAT_MEMORY("chat:memory:");

    private final String value;
}
