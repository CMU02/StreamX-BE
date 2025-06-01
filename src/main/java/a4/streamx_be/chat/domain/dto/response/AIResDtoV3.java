package a4.streamx_be.chat.domain.dto.response;

import a4.streamx_be.chat.domain.model.Emotion;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AIResDtoV3(
        @JsonProperty("aiText") String aiText,
        @JsonProperty("emotion") Emotion emotion,
        @JsonProperty("audioUrl") String audioUrl,
        @JsonProperty("animation") String animation
) { }
