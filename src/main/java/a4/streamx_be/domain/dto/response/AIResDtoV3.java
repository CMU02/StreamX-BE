package a4.streamx_be.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AIResDtoV3(
        @JsonProperty("aiText") String aiText,
        @JsonProperty("emotion") Emotion emotion,
        @JsonProperty("audioUrl") String audioUrl,
        @JsonProperty("animation") String animation
) { }
