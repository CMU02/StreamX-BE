package a4.streamx_be.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Emotion(
        @JsonProperty("Joy") double joy,
        @JsonProperty("Sorrow") double sorrow,
        @JsonProperty("Surprised") double surprised,
        @JsonProperty("Angry") double angry,
        @JsonProperty("Fun") double fun
) { }
